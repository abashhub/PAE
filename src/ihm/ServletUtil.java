package ihm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import bizz.dto.UserDTO;
import config.Configuration;

public abstract class ServletUtil {

  private static Logger logger = Logger.getLogger(ServletUtil.class);
  private final static String JWTSecret = Configuration.properties.getProperty("jwtsecret");
  private final static Genson genson =
      new GensonBuilder().useDateFormat(new SimpleDateFormat("yyyy-MM-dd")).useIndentation(true)
          .useConstructorWithArguments(true).exclude("password").create();



  static void updateSession(HttpServletRequest req, HttpServletResponse resp, UserDTO user) {
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("id", user.getLogin());
    req.getSession().setAttribute("id", user.getLogin());
    claims.put("ip", req.getRemoteAddr());
    req.getSession().setAttribute("ip", req.getRemoteAddr());

    String token = new JWTSigner(JWTSecret).sign(claims);
    logger.debug("**** TOKEN **** ---> " + token);

    Cookie cookie = new Cookie("user", token);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24);
    resp.addCookie(cookie);
  }

  static String isLogged(HttpServletRequest req) {
    Object idUser = req.getSession().getAttribute("id");
    String idJWT = null;
    String logged = "user";

    if (idUser == null) {
      String token = getCookie(req);
      try {
        Map<String, Object> decodedPayload = new JWTVerifier(JWTSecret).verify(token);
        idJWT = (String) decodedPayload.get("id");
        if (!req.getRemoteAddr().equals(decodedPayload.get("ip")))
          idJWT = null;
      } catch (Exception exc) {
        // ignore
      }
      if (idJWT != null) {
        req.getSession().setAttribute("id", idJWT);
      } else {
        // no valid user found
        logged = "not logged";
      }
    }
    return logged;
  }

  private static String getCookie(HttpServletRequest req) {
    String token = null;
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie c : cookies) {
        if ("user".equals(c.getName()) && c.getSecure()) {
          token = c.getValue();
        } else if ("user".equals(c.getName()) && token == null) {
          token = c.getValue();
        }
      }
    }
    return token;
  }

  public static void handleError(HttpServletResponse resp, String message, int errorCode)
      throws IOException {
    Map<String, Object> errorMap = new HashMap<String, Object>();
    logger.error("[" + errorCode + "] " + message);
    errorMap.put("status", "error");
    errorMap.put("message", message);
    resp.setStatus(errorCode);
    String json = genson.serialize(errorMap);
    ServletUtil.send(resp, json);
  }

  @Deprecated
  static void toFrontEnd(HttpServletResponse resp, boolean test, String success, String error,
      String dataName, Object data, int errorCode) throws IOException {
    Map<String, Object> map = new HashMap<String, Object>();
    if (test) {
      map.put("status", "success");
      map.put("message", success);
      map.put(dataName, data);
      resp.setStatus(200);
    } else {
      map.put("status", "error");
      map.put("message", error); // TODO message should contain
      resp.setStatus(errorCode);
    }
    String json = genson.serialize(map);
    ServletUtil.send(resp, json);
  }

  @Deprecated
  static void toFrontEnd(HttpServletResponse resp, boolean test, String success, String error,
      int errorCode) throws IOException {
    Map<String, Object> map = new HashMap<String, Object>();
    if (test) {
      map.put("status", "success");
      map.put("message", success);
      resp.setStatus(200);
    } else {
      map.put("status", "error");
      map.put("message", error); // TODO message should contain
      resp.setStatus(errorCode);
    }
    String json = genson.serialize(map);
    ServletUtil.send(resp, json);
  }

  @Deprecated
  static void toFrontEnd(HttpServletResponse resp, boolean test, String success, String error,
      Map<String, Object> args, int errorCode) throws IOException {
    Map<String, Object> map = new HashMap<String, Object>();
    if (test) {
      map.put("status", "success");
      map.putAll(args);
      resp.setStatus(200);
    } else {
      map.put("status", "error");
      map.put("message", error); // TODO message should contain
      resp.setStatus(errorCode);
    }
    String json = genson.serialize(map);
    ServletUtil.send(resp, json);

  }

  // sans exception à partir d'ici
  public static void toFrontEnd(HttpServletResponse resp, String success, String dataName,
      Object data) throws IOException {
    logger.info("[200] " + success);
    logger.debug(dataName + "=" + data);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", "success");
    map.put("message", success);
    map.put(dataName, data);
    resp.setStatus(200);
    String json = genson.serialize(map);
    send(resp, json);
  }

  static void toFrontEnd(HttpServletResponse resp, String success, Map<String, Object> args)
      throws IOException {
    logger.info("[200] " + success);
    logger.debug(args);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", "success");
    map.putAll(args);
    resp.setStatus(200);
    String json = genson.serialize(map);
    send(resp, json);
  }


  static void toFrontEnd(HttpServletResponse resp, String success) throws IOException {
    logger.info("[200] " + success);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", "success");
    map.put("message", success);
    resp.setStatus(200);
    String json = genson.serialize(map);
    send(resp, json);
  }

  // celle ci est utilisée par les autres et par les méthodes qui n'uilise pas le
  // toFrontEnd classique

  public static void send(HttpServletResponse resp, String json) throws IOException {
    resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
    byte[] messageBytes = json.getBytes(StandardCharsets.UTF_8);
    resp.setContentLength(messageBytes.length);
    logger.debug("contentLength=" + messageBytes.length);
    resp.getOutputStream().write(messageBytes);
  }
}

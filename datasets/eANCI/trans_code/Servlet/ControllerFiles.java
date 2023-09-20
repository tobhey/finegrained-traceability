package Servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

import com.oreilly.servlet.*;

/**
 * The ControllerFiles class manages the upload of a file The class has no dependencies
 *
 * @author Francesco Odierna
 */

public class ControllerFiles extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // instantiate the variables
    // The ServletContext sevir� to get the MIME type of the uploaded file
    ServletContext context = getServletContext();
    String forward = null;
    try {
      // We establish the maximum size of the file we want to upload
      int maxUploadFile = 500000000;
      MultipartRequest multi = new MultipartRequest(request, ".", MaxUploadFile);
      String description = multi.getParameter("text");
      File myFile = multi.getFile("myFile");
      String filePath = multi.getOriginalFileName("myFile");
      String path = "C: \\ RequestsCambiResidence \\";
      try {
        // recast the file data using an InputStream
        FileInputStream inStream = new FileInputStream(myFile);
        // we decide where the file will go
        FileOutputStream outStream = new FileOutputStream(path + myFile.getName());
        // salstreetmo the file to the specified path
        while (inStream.available() > 0) {
          outStream.write(inStream.read());
        }
        // close the streams
        inStream.close();
        outStream.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      forward = "/workers/index.jsp?func=upload&page=done";
      // we put the data in the request so that we can get it from the jsp
      request.setAttribute("contentType", context.getMimeType(path + myFile.getName()));
      request.setAttribute("text", description);
      request.setAttribute("path", path + myFile.getName());
      RequestDispatcher rd = request.getRequestDispatcher(forward);
      rd.forward(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
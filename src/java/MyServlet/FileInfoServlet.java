/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyServlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Eugene
 */
@MultipartConfig
public class FileInfoServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FileInfoServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FileInfoServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter output = response.getWriter();
        String fileType = request.getContentType();
        byte[] buffer = new byte[8 * 1024];
        byte[] hashBuffer = new byte[8 * 1024];
        byte[] hash;
        int bytesRead;
        int hashBytesRead;
        String hashValue;
        boolean dirFlag = false;
        MessageDigest digest;
        Part filePart = request.getPart("file");
        String fileExtensionName = getFileExtension(filePart);
//        output.println(fileExtensionName);
        String filePath = "C:\\Users\\Eugene\\Documents\\"
                + "NetBeansProjects\\ForLearning\\FileWrite";
        
            
        try{
        if (fileType != null && fileType.contains("multipart/form-data")) {
                response.setContentType("text/plain");
                output.println("File Uploaded Successfully! ");
                digest = MessageDigest.getInstance("SHA-256");
                InputStream inputStream = filePart.getInputStream();
                InputStream hashInputStream = filePart.getInputStream();
                while((hashBytesRead = hashInputStream.read(hashBuffer)) != -1) {
                        digest.update(hashBuffer, 0, hashBytesRead);
                }
                hash = digest.digest();
                hashValue = new BASE64Encoder().encode(hash);
                output.println(hashValue);
                File targetFile = new File(filePath);
                if(targetFile.isDirectory()){
                    targetFile = new File(filePath, hashValue + "." + fileExtensionName);
                    dirFlag = true;
                }
                if(dirFlag){
                    output.println("Directory created successfully");
                    targetFile.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(targetFile);
                    while((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                outputStream.close();
            }
                
        } else
            output.println("Upload File Fail!");
        }
        catch(Exception e){
        }
//    }
//        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    private String getFileExtension(Part part){
        String partHeader = part.getHeader("Content-Disposition");
        for(String content: partHeader.split(";")){
            if(content.trim().startsWith("filename")){
                return content.substring(
                        content.indexOf(".") + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}

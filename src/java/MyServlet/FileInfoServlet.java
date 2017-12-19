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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
    Set<String> fileSet;
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
        Part filePart = request.getPart("file");
        String filePath = "C:\\Users\\Eugene\\Desktop\\GitRepository"
                + "\\DependencyCheck\\DependencyCheck\\Queue";

        try {
            if (fileType != null && fileType.contains("multipart/form-data")) {
                response.setContentType("text/plain");
                output.println("File Uploaded Successfully! ");
                File targetFile = new File(filePath);
                if (targetFile.exists()) {
                    output.println("TargetFile exist");
                    output.println("Directory created successfully");
                    createFile(filePath, filePart, output);
                } else {
                    output.println("TargetFile does not exist");
                }

            } else {
                output.println("Upload File Fail!");
            }
        } catch (Exception e) {
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

    private String getFileExtension(Part part) {
        String partHeader = part.getHeader("Content-Disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf(".") + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private void createFile(String filePath, Part filePart, PrintWriter output) {
        File targetFile = null;
        InputStream inputStream;
        FileOutputStream outputStream;
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        String fileExtensionName;
        String hashValue;
        String hashFileName;
        try {
            fileSet = storeFileNameInHashSet();
            inputStream = filePart.getInputStream();
            fileExtensionName = getFileExtension(filePart);
            hashValue = hashFileName(filePart, output);
            hashFileName = hashValue + "." + fileExtensionName;
//            for(String s: fileSet){
//                output.println(s);
//            }
            if (!searchFile(hashFileName)) {
                output.println("True!");
                targetFile = new File(filePath, hashFileName);
                targetFile.createNewFile();
                outputStream = new FileOutputStream(targetFile);
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                outputStream.close();
            }
            else{
                output.println("False!");
            }
        } catch (Exception e) {

        }

    }

    private String hashFileName(Part filePart, PrintWriter output) {
        MessageDigest digest;
        InputStream inputStream;
        byte[] buffer = new byte[8 * 1024];
        byte[] hash = null;
        String hashValue;
        int bytesRead;

        try {
            inputStream = filePart.getInputStream();
            digest = MessageDigest.getInstance("SHA-256");
            output.println();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            hash = digest.digest();
            hashValue = base16Encoder(hash);

            return hashValue;
        } catch (Exception e) {

        }
        return null;
    }

    private String base16Encoder(byte[] hash) {
        int quotient;
        int remainder;
        String hexadecimal = "";
        String reverse = "";
        String[] hexAlpha = {"a", "b", "c", "d", "e", "f"};
        for (Byte b : hash) {
            if (b < 0) {
                quotient = b + 256;
            } else {
                quotient = b;
            }
            while (quotient != 0) {
                remainder = quotient % 16;
                if (remainder > 9) {
                    reverse += hexAlpha[remainder - 10];

                } else {
                    reverse += Integer.toString(remainder);
                }
                quotient /= 16;
            }
            hexadecimal += new StringBuffer(reverse).reverse().toString();
            reverse = "";
        }
        return hexadecimal;
    }
    private Set<String> storeFileNameInHashSet(){
        String path = "C:\\Users\\Eugene\\Desktop\\"
                + "GitRepository\\DependencyCheck\\DependencyCheck\\Complete";
        Set<String> files = new HashSet<String>();
        File directory = new File(path);
        File[] directoryFiles = directory.listFiles();
        
        String filename;
        
        for(int i = 0; i < directoryFiles.length; i++){
            filename = directoryFiles[i].getName();
            files.add(filename);
        }
        return files;
    }
    
    public boolean searchFile(String filename){
        return fileSet.contains(filename);
    }

}

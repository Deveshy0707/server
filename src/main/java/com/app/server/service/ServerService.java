package com.app.server.service;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//import com.github.jsm.frontend.Javancss;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;
// import java.io.File; // Not needed for JAXB parsing directly


@Service
public class ServerService {

    String gradle="C:\\softw\\gradle\\gradle-8.3\\bin";
    public int evaluate(byte[] fileContent){


        save(fileContent);

        return unitTests("") + apiCalls();
    }

    public void save(byte[] fileContent){

        try {
            // Define the directory where you want to save the uploaded files
            //////////////////
            /////////////////
            ///////////////
            String uploadDirectory = "C:\\Users\\devesyad\\Downloads\\projectserver\\DB\\testingProject";

            // Create the directory if it doesn't exist
            File uploadDir = new File(uploadDirectory);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Create a unique file name (you can modify this based on your requirements)
            //String fileName = "uploaded_" + System.currentTimeMillis() + ".zip";
            String fileName = "demo.zip";

            // Create a FileOutputStream to save the byte array to a file
            try (FileOutputStream fileOutputStream = new FileOutputStream(new File(uploadDirectory, fileName))) {
                fileOutputStream.write(fileContent);
            }

            unzipFile(uploadDirectory, "C:\\Users\\devesyad\\Downloads\\projectserver\\DB\\testingProject\\demo.zip");


        } catch (Exception e) {
            System.out.println(e);
        }

        String with="C:\\Users\\devesyad\\Downloads\\projectserver\\test";
        replace("C:\\Users\\devesyad\\Downloads\\projectserver\\DB\\testingProject\\demo\\src\\test", with);
    }

    public void unzipFile(String outdir, String zipFilePath) throws IOException {
        //////////////////
        /////////////////
        ////////////////
        //use for linux, also use thread
        //////////////
        //ProcessBuilder builder=new ProcessBuilder("cmd","/c", "tar -xf "+zipFilePath);
        //why belwo one not working and throwing errors
//        ProcessBuilder builder=new ProcessBuilder("C:\\softw\\7zip\\x64\\7za.exe x "+zipFilePath+" -o"+outdir); //you can pass a string too
        //thread for this??
        Runtime builder=Runtime.getRuntime();
//        System.out.println(builder.command());
//        Process process=builder.start();
        Process process=builder.exec("C:\\softw\\7zip\\x64\\7za.exe x "+zipFilePath+" -o"+outdir);
        //or thread for this??
        try {
            System.out.println(process.waitFor());
        }
        catch(Exception e){
            System.out.println(e);
        }

    }

    public void replace(String to, String with){
        //we can also overwrite xcopy <source> <destination> /<options> with /Y option
        //or can do something like git merge

//        Process process=null;
        try{
//            ProcessBuilder builder=new ProcessBuilder("rmdir","/s","/q", to);
//            Process process=builder.start();
            Runtime builder=Runtime.getRuntime();
            Process process=builder.exec("rmdir "+"/s "+"/q "+to);
            System.out.println(process.waitFor());


            process=builder.exec("md  "+to);
            process.waitFor();

            process=builder.exec("xcopy "+"/E "+with+" "+to);
            process.waitFor();


        }
        catch(Exception e){
            System.out.println(e);
        }

//        builder=new ProcessBuilder("md", to);
//        try{
//            Process process=builder.start();
//            System.out.println(process.waitFor());
//        }
//        catch(Exception e){
//            System.out.println(e);
//        }
//
//        builder=new ProcessBuilder("xcopy","/E", with, to);
//        try{
//            Process process=builder.start();
//            System.out.println(process.waitFor());
//        }
//        catch(Exception e){
//            System.out.println(e);
//        }
    }

    public int unitTests(String path){
        path="C:\\Users\\devesyad\\Downloads\\projectserver\\DB\\testingProject\\demo";
        try{
            Runtime builder=Runtime.getRuntime();
            //Using the -p or --project-dir option
            Process process=builder.exec(gradle+"\\gardle -p "+path+" build");
            process.waitFor();

            //Using the -p or --project-dir option
            process=builder.exec(gradle+"\\gardle -p "+path+" test");
            process.waitFor();
        }
        catch(Exception e){
            System.out.println(e);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=null;
        try {
            builder= factory.newDocumentBuilder();
        } catch (Exception e) {
            System.out.println(e);
        }
        File reportFile = new File("C:\\Users\\devesyad\\Downloads\\projectserver\\DB\\testingProject\\demo\\build\\reports\\tests\\test", "index.html");
        Document doc=null;
        try {
            doc = builder.parse(reportFile);
        } catch (Exception e) {
            System.out.println(e);
        }
        Element root = doc.getDocumentElement();
        NodeList list=root.getElementsByTagName("testsuite");
        Node node=list.item(0);

        NamedNodeMap map=node.getAttributes();
        System.out.println(map.getNamedItem("name"));
        System.out.println(map.getNamedItem("tests"));
        System.out.println(map.getNamedItem("failures"));

        NodeList nl=node.getChildNodes().item(0).getChildNodes();
        for(int i=0; i<nl.getLength(); i++){
            boolean b=nl.item(i).hasChildNodes();
            if(b){
                System.out.println("failed");
            }
            NamedNodeMap m=nl.item(i).getAttributes();
            System.out.println(m.getNamedItem("name"));
            System.out.println(m.getNamedItem("classname"));

        }
//        try {
//            byte[] reportContent = Files.readAllBytes(reportFile.toPath());
//            String reportString = new String(reportContent, StandardCharsets.UTF_8); // Convert bytes to String
//            Javancss parser = new Javancss();
//
//// Extract specific elements or attributes as needed
//            List<String> testResults = parser.query("#results li.passed"); // Example: Get passed test counts
//
//            String packageName = "com.app.server"; // Replace with your package name
//            JAXBContext context = JAXBContext.newInstance(packageName);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//
//            Report report = (Report) unmarshaller.unmarshal(new ByteArrayInputStream(reportContent));
//
//            // Process the report data (e.g., log, display, store in database)
//            System.out.println("Number of passed tests: " + report.getPassedTests());
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//        Document doc = Jsoup.connect("C:\\Users\\devesyad\\Downloads\\projectserver\\DB\\testingProject\\demo\\build\\reports\\tests\\test\\index.html").get();
//        doc.select("p").forEach(System.out::println);

        return 0;
    }

    public int apiCalls(){

        return 0;
    }
}

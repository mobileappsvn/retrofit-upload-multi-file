package com.robert.uploadfile.tmp;

/*public static void main(String[]args){
        String charset="UTF-8";
        File uploadFile1=new File("e:/Test/PIC1.JPG");
        File uploadFile2=new File("e:/Test/PIC2.JPG");
        String requestURL="http://localhost:2011//upload_multi_files/fileUpload.php";

        try{
            MultipartUtility multipart=new MultipartUtility(requestURL,charset);

            multipart.addHeaderField("User-Agent","CodeJava");
            multipart.addHeaderField("Test-Header","Header-Value");

            multipart.addFormField("Username","TestUser");

            multipart.addFilePart("uploaded_file[]",uploadFile1);
            multipart.addFilePart("fileUpload[]",uploadFile2);

            String response=multipart.finish();

        } catch(IOException ex){
            System.err.println(ex);
        }
}*/
<?php
  
    $file_path = "uploads/";
    $username = $_POST['Username'];
     
     /*
     Single upload
    $file_path = $file_path . basename( $_FILES['uploaded_file']['name']);
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        echo "success user ".$username;
    } else{
        echo "fail user ".$username;
    }
    */
    
    	//Loop through each file
    	
    	 $success = array();
	 $failure = array();
	    
	for($i=0; $i<count($_FILES['uploaded_file']['name']); $i++) {
	  //Get the temp file path
	  $tmpFilePath = $_FILES['uploaded_file']['tmp_name'][$i];
	
	  //Make sure we have a filepath
	  if ($tmpFilePath != ""){
	    //Setup our new file path
	    $newFilePath = "uploads/" . $_FILES['uploaded_file']['name'][$i];
	
	
	   
	    //Upload the file into the temp dir
	    if(move_uploaded_file($tmpFilePath, $newFilePath)) {
		$success[] = $newFilePath;
	    }else{
	    	$failure[] = $newFilePath;
	    }
	    
	    
	  }
	}
	
	
	$result = array("Username" => $username, "Success"=>$success,"Failure"=>$failure);
	
	echo json_encode($result);
	

 ?>
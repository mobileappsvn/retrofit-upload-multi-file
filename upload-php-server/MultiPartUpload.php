<?php
ini_set('display_errors',1);
ini_set('upload_max_filesize', '20M');
ini_set('post_max_size', '20M');
ini_set('max_input_time', 30000);
ini_set('max_execution_time', 30000);

//var_dump($_FILES);
//var_dump($_FILES ['file']);
$result = array("success" => "OKOK");

for($i = 0; $i < count ( $_FILES ['file'] ['name'] ); $i ++) {
	try {
		if (move_uploaded_file( $_FILES ['file'] ["tmp_name"][$i], "uploads/" . $_FILES ["file"] ["name"][$i] )) {
			
			$result = array("success" => "File successfully uploaded");

		} else {
			$result = array("success" => "error uploading file");
			throw new Exception('Could not move file');
		}
	} catch (Exception $e) {
    	die('File did not upload: ' . $e->getMessage());
	}
	echo json_encode($result, JSON_PRETTY_PRINT);

}
?>

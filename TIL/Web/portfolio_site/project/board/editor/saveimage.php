
<script type="text/javascript">
  alert('들어왔다');
</script>

<?php

// echo "<pre>";
// var_dump($_FILES);
// echo "</pre>";

$allowed_ext = array('jpg','jpeg','png','gif');

$name = $_FILES['image']['name'];
$error = $_FILES['image']['error'];
$ext = array_pop(explode('.', $name));

if(!in_array($ext, $allowed_ext)) {
	echo "허용되지 않는 확장자입니다.";
	exit;
}

if( $error != UPLOAD_ERR_OK ) {
	switch( $error ) {
		case UPLOAD_ERR_INI_SIZE:
		case UPLOAD_ERR_FORM_SIZE:
			echo "파일이 너무 큽니다. ($error)";
			break;
		case UPLOAD_ERR_NO_FILE:
			echo "파일이 첨부되지 않았습니다. ($error)";
			break;
		default:
			echo "파일이 제대로 업로드되지 않았습니다. ($error)";
	}
	exit;
}

if(move_uploaded_file($_FILES['image']['tmp_name'], 'images/'.$name)){
    echo 'images/'.$name;
} else {
    echo "이미지 업로드를 실패하였습니다.";
}

?>

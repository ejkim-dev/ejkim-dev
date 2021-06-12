<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

  $title = $_POST['title'];
  $category = $_POST['category'];
  $public = $_POST['public'];
  $content = $_POST['summernote'];

  // $thumbnail = $_POST['imgFile'];
  // echo "<pre>";
  // var_dump($_FILES); // 이미지 파일 확인용
  // echo "</pre>";

  /* 결과 주석 : 결과 이상 없음!
  array(1) {
  ["imgFile"]=>
  array(5) {
    ["name"]=>
    string(17) "destination-1.jpg"
    ["type"]=>
    string(10) "image/jpeg"
    ["tmp_name"]=>
    string(14) "/tmp/phpEmXY6X"
    ["error"]=>
    int(0)
    ["size"]=>
    int(230751)
  }
}*/

    // 임시저장된 정보 : 전송된 파일은 $_FILES를 통해 접근 가능
    $myTempFile = $_FILES['imgFile']['tmp_name'];
    // echo " | myTempFile =  {$myTempFile}"; //  /tmp/php31dfqx
    // echo "<br>";
    $fileName = $_FILES['imgFile']['name'];//파일 이름(확장자 포함)
    // echo " | fileName =  {$fileName}"; //  /tmp/php31dfqx
    // echo "<br>";
    // 파일 타입 및 확장자 구하기
    $fileTypeExtension = explode("/", $_FILES['imgFile']['type']);
    // echo " | fileTypeExtension = {$fileTypeExtension}"; // Array
    // echo "<br>";

    // 파일 타입
    $fileType = $fileTypeExtension[0];
    // echo " | fileType = {$fileType}"; // image
    // echo "<br>";

    // 파일 확장자
    $extension = $fileTypeExtension[1];
    // echo " | extension = {$extension}"; // jpeg
    // echo "<br>";

    //확장자 검사
    $isExtGood = false;

    switch($extension){
        case 'jpeg':
        case 'bmp':
        case 'gif':
        case 'png':
        $isExtGood = true; // 확장자 확인 완료
        break;
        default : // 허용된 확장자 외 다른 확장자
            // echo "허용하는 확장자는 jpg, bmp, gif, png 입니다. - switch";
            // exit;
            //미리보기 이미지 선택 안했을 때 저장되는 경로
            $thumbnail = "/blog/images/destination-9.jpg";
            break;
    }

    //이미지 파일이 맞는지 확인
    if($fileType == 'image'){
        // 허용할 확장자를 jpg, bmp, gif, png로 정함 그외는 업로드 불가
        if($isExtGood){
            // 임시 파일 옮길 저장 및 파일명
            $nowTime = time();
            // echo " | nowTime = {$nowTime}"; // jpeg
            // echo "<br>";
            $imgname = $nowTime."_".$fileName;

            // 상위 폴더(board)에서 images 찾아서 저장하세요
            $filePath = "../images/$imgname"; // 결과 : 1580274370_about.jpg
            $thumbnail = "/board/images/$imgname"; // 절대경로
            // echo " | filePath = {$filePath}";
            // echo "<br>";
            // echo " | thumbnail = {$thumbnail}";
            // echo "<br>";
            // 임시 저장된 파일을 우리가 저장할 장소 및 파일명으로 옮김
            // move_uploaded_file()은 서버로 전송된 파일을 저장할 때 사용하는 함수
            $imageUpload = move_uploaded_file($myTempFile,$filePath);
            // echo " | imageUpload = {$imageUpload}";
            // echo "<br>";
            //업로드 성공 여부 확인
            if($imageUpload == true){
                // echo '파일이 정상적으로 업로드 되었습니다. <br>';
                // echo "<img src='{$filePath}' width='100'/>";//저장된 파일 넓이 100으로 불러오기
            }else{
                echo '미리보기 이미지 업로드에 실패했습니다. ';
            }
        }
        //미리보기 이미지가 있지만 확장자가 jpg, bmp, gif, png가 아닐때
        else{
            echo "허용하는 확장자는 jpg, bmp, gif, png 입니다. - else";
            exit;
        }
    }

    // type이 image가 아닐때
    // else{
    //     echo "이미지 파일이 아닙니다. ";
    //     // exit;
    // }

// 제목
  if ($title != null && $title != '') {
    $title = $dbConnect->real_escape_string($title);
  } else {
    echo "title = ".$title;
    echo "내용을 입력하세요.";
    echo "<a href='./index.php'>작성 페이지로 이동</a>";
    exit;
  }

// 카테고리
  if ($category != null && $category != '') {
    // 1. 개발, 2. 여행, 3. 일상
    $category = $dbConnect->real_escape_string($category);
    if ($category == 1) {
      $category = "개발";
    } else if ($category == 2) {
      $category = "여행";
    } else {
      $category = "일상";
    }
  } else {
    echo "category = ".$category;
    echo "내용을 입력하세요.";
    echo "<a href='./index.php'>작성 페이지로 이동</a>";
    exit;
  }

// 위랑 내용 똑같은데? 왜있지?
  // if ($category != null && $category != '') {
  //   // 1. 개발, 2. 여행, 3. 일상
  //   $category = $dbConnect->real_escape_string($category);
  //   if ($category == 1) {
  //     $category = "개발";
  //   } else if ($category == 2) {
  //     $category = "여행";
  //   } else {
  //     $category = "일상";
  //   }
  // } else {
  //   echo "category = ".$category;
  //   echo "내용을 입력하세요.";
  //   echo "<a href='./index.php'>작성 페이지로 이동</a>";
  //   exit;
  // }

// 공개여부
  if ($public != null && $public != '') {
    $public = $dbConnect->real_escape_string($public);
  } else {
    echo "content = ".$public;
    echo "내용을 입력하세요.";
    echo "<a href='./index.php'>작성 페이지로 이동</a>";
    exit;
  }

  $memberID = $_SESSION['memberID'];
  $regTime = time();

// , thumbnail를 마지막에 추가해야함
  $sql = "INSERT INTO board (memberID, title, category, content, regTime, checked, thumbnail)";
  // 값에 thumbnail 의 값 추가해야함
  $sql .= "VALUES ({$memberID}, '{$title}', '{$category}', '{$content}', {$regTime}, '{$public}', '{$thumbnail}')";
  $result = $dbConnect->query($sql);

  if ($result) {
      echo "저장 완료";
      echo "<br>";
      echo "<a href='/sovpdlwl'>어드민 페이지 이동</a>";
      exit;
  } else {
    echo "저장 실패 : 관리자에게 문의";
    echo "<br>";
    echo "<a href='/sovpdlwl'>어드민 페이지 이동</a>";
    exit;
  }

 ?>

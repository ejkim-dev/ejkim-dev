<!-- 블로그 포스트 내용 수정 시 사용 -->
<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

  $boardID = $_GET['boardID'];
  $title = $_POST['title'];
  $category = $_POST['category'];
  $public = $_POST['public'];
  $content = $_POST['summernote'];

  // sql에서 썸네일 값 가져오기
  $mysql = "SELECT * FROM board WHERE boardID = {$boardID}";
  $myresult = $dbConnect->query($mysql);
  $blogedit = $myresult->fetch_array(MYSQLI_ASSOC);// MYSQLI_ASSOC : 컬럼명으로 값을 불러옴
  $mythumbnail = $blogedit['thumbnail'];


//미리보기이미지
  // 임시저장된 정보 : 전송된 파일은 $_FILES를 통해 접근 가능
  $myTempFile = $_FILES['imgFile']['tmp_name'];
  $fileName = $_FILES['imgFile']['name'];//파일 이름(확장자 포함)
  // 파일 타입 및 확장자 구하기
  $fileTypeExtension = explode("/", $_FILES['imgFile']['type']);
  // 파일 타입
  $fileType = $fileTypeExtension[0];
  // 파일 확장자
  $extension = $fileTypeExtension[1];
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
          // $thumbnail = $_POST['thumbnail'];
          $thumbnail = "$mythumbnail";
          // echo "$thumbnail";
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

  // echo "boardID = ".$boardID;

  // $regTime = time();

  //업데이트 내용 저장함
  $sql = "UPDATE board SET title = '{$title}', category ='{$category}', content ='{$content}', checked ='{$public}', thumbnail='{$thumbnail}' WHERE boardID = {$boardID} ";
  $result = $dbConnect->query($sql);

  if ($result) {
      echo "저장 완료";
      echo "<br>";
      echo "<a href='/blog'>블로그로 이동</a>";
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

<!-- 연습용 나중에 삭제 -->
<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
 ?>
<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <title>이미지 저장 폼</title>
  </head>
  <body>
    <form name = "fileUpload" action="./imageUpload.php" method="post"
          enctype="multipart/form-data">
          <input type="file" name="imgFile" />
          <input type="submit" value="업로드"/>
    </form>

  </body>
</html>

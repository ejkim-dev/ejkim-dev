<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  if (isset($_SESSION['memberID'])||isset($_COOKIE['autologin'])){
    Header("Location:../sovpdlwl/");//로그인 정보 있으면 어드민 메인페이지
  }
 ?>
<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>로그인페이지</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900" rel="stylesheet">
    <!-- css 파일 수정 후 적용 안될 때 대처 : ?after 부분을 추가해줍니다. 문자열은 아무거나 상관이 없습니다. 요는 브라우저가 다른 CSS를 인식하게끔만 하면 되니까요.-->
    <link rel="stylesheet" href="/blog/css/style.css">

  </head>
  <body>
    <div class="comment-form-wrap col-4 offset-4 ">

<!-- 로그인 정보 form 오로 넘기기 -->
      <form name="signIn" action="./signInProcessing.php" class="p-5 bg-light" method="post">
        <h3 class="mb-5 text-center">Login</h3>
        <div class="form-group">
          <label for="email">Email *</label>
          <input type="email" class="form-control" name="userEmail"
          <?php
          if(isset($_COOKIE['memberID'])) {
           echo "value={$_COOKIE['memberID']}";
           } ?>  required />

        </div>
        <div class="form-group">
          <label for="password">Password *</label>
          <input type="password" class="form-control" name="userPw" required />
        </div>
        <div class="form-group text-center">
          <input type="checkbox" class="form-check-inline" name="idSaveCheck" value="Remember" checked /> Remember Your ID
        </div>
        <div class="form-group ">
          <input type="submit" value="LOG IN" class="btn py-3 btn-block btn-primary">
        </div>
        <div class="form-group text-center">
          <!-- <input type="text" value="LOG IN" class="btn py-3 btn-block btn-primary"> -->
          <a class="mb-5" name="getid" href="#">Forgot your password?</a>
        </div>

      </form>

    </div>

  </body>
</html>

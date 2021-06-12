<?php
  // include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  if (isset($_SESSION['memberID'])){
    Header("Location:./index.php");//로그인 정보 있으면 어드민 메인페이지
  }
 ?>
<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
  <?php
  include $_SERVER['DOCUMENT_ROOT'].'/maumadmin/include/head.php';
   ?>
  </head>
  <body>
    <div class="comment-form-wrap col-4 offset-4 ">

<!-- 로그인 정보 form 오로 넘기기 -->
      <form name="signIn" action="employee_login_query.php" class="p-5 bg-light" method="post">
        <h3 class="mb-5 text-center">Login</h3>
        <div class="form-group">
          <label for="userId">ID *</label>
          <input type="number" class="form-control" name="user_email" required />

        </div>
        <div class="form-group">
          <label for="password">Password *</label>
          <input type="password" class="form-control" name="user_pw" required />
        </div>
        <!-- <div class="form-group text-center">
          <input type="checkbox" class="form-check-inline" name="idSaveCheck" value="Remember" checked /> Remember Your ID
        </div> -->

        <div class="form-group py-3 text-center">
          <!-- <input type="text" value="LOG IN" class="btn py-3 btn-block btn-primary"> -->
          <!-- <a class="mb-5" name="getid" href="#">Forgot your password?</a> -->
        </div>

        <div class="form-group ">
          <input type="submit" value="LOG IN" class="btn py-3 btn-block btn-primary">
        </div>


      </form>

    </div>

  </body>
</html>

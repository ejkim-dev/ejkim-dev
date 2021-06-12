<!-- 관리자외 접근 막음 -->
<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
 ?>
<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>회원가입</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900" rel="stylesheet">
    <link rel="stylesheet" href="/blog/css/style.css">
  </head>
  <body>

    <div class="comment-form-wrap col-4 offset-4 ">

      <form name="signUp" action="/signUp/signUpSave.php" class="p-5 bg-light" method="post">
        <h3 class="mb-5 text-center">Sign Up</h3>
        <div class="form-group">
          <label for="email">Email *</label>
          <input type="email" class="form-control" name="userEmail" required />
        </div>
        <div class="form-group">
          <label for="text">NickName *</label>
          <input type="text" class="form-control" name="userNickName" required />
        </div>
        <div class="form-group">
          <label for="password">Password *</label>
          <input type="password" class="form-control" name="userPw" required />
        </div>

        <div class="form-group">
          <label for="text">Birth *</label>
          <br>
          <select class="form-control-lg" name="birthYear" required>
        <?php
          $thisYear = date('Y', time());
            for ($i=$thisYear; $i >= 1960; $i--) {
              echo "<option value='{$i}'>{$i}</option>";
            }
         ?>
       </select>년&emsp;
          <select class="form-control-lg" name="birthMonth" required>
        <?php
            for ($i=1; $i <= 12; $i++) {
              echo "<option value='{$i}'>{$i}</option>";
            }
         ?>
       </select>월&emsp;
       <select class="form-control-lg" name="birthDay" required>
     <?php
         for ($i=1; $i <= 31; $i++) {
           echo "<option value='{$i}'>{$i}</option>";
         }
      ?>
          </select>일
        </div>

        <div class="form-group ">
          <input type="submit" value="Sign Up" class="btn py-3 btn-block btn-primary">
        </div>

      </form>

    </div>

  </body>
</html>

<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
 ?>
<nav class="navbar navbar-expand-lg navbar-dark ftco_navbar bg-dark ftco-navbar-light" id="ftco-navbar">
  <div class="container">
    <!-- 오른쪽 로고 -->
    <a class="navbar-brand" href="/index.php">EJKIM<span>Portfolio</span></a>
      <!-- Menu의 버튼 -->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#ftco-nav" aria-controls="ftco-nav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="oi oi-menu"></span> Menu
    </button>

    <div class="collapse navbar-collapse" id="ftco-nav">
      <ul class="navbar-nav ml-auto">
        <li class="nav-item"><a href="/index.php" class="nav-link">Home</a></li>
    <!--blog 버튼 노란색 유지-->
        <li class="nav-item active"><a href="/blog/" class="nav-link">Blog</a></li>
        <li class="nav-item" ><a href="/index.php#contact" class="nav-link">Contact</a></li>

        <?php if (isset($_SESSION['memberID'])||isset($_COOKIE['autologin'])){
           ?>
        <li class="nav-item cta" id = "loginBtn"><a href="/signIn/signOut.php" class="nav-link"> Logout </a></li>
           <?php
           }
            ?>
        <!-- 로그인 버튼 -->
        <!-- <li class="nav-item cta"><a href="#" class="nav-link">Login</a></li> -->
        <!-- <li class="nav-item cta" id = "loginBtn"><a href="/signIn/signInForm.php" class="nav-link"> Login </a></li> -->

      </ul>
    </div>
  </div>
</nav>

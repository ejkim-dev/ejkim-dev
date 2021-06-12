<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속
 ?>
<!DOCTYPE html>
<html lang="en">
  <head>
<?php include_once './include/head.php'; ?>
  </head>
  <body>
    <header><?php include_once './include/header.php'; ?></header>
    <!-- END nav -->

    <section class="hero-wrap hero-wrap-2 js-fullheight" data-stellar-background-ratio="0.5">
      <div class="overlay"></div>
      <div class="container">
        <div class="row no-gutters slider-text js-fullheight align-items-end justify-content-center">
          <div class="col-md-9 ftco-animate pb-5 text-center">
            <h1 class="mb-3 bread">Blog Details</h1>
          </div>
        </div>
      </div>
    </section>

    <section class="ftco-section ftco-no-pt ftco-no-pb" id="contents">
      <div class="container">
        <div class="row">
          <div class="col-lg-8 order-md-last ftco-animate py-md-5 mt-md-5">

            <?php
        // var_dump( $_SESSION );//세션 전체 값 불러오기
              if (isset($_GET['boardID']) && (int) $_GET['boardID'] > 0) {// $_GET['boardID']가 존재하고 0을 초과하는지 확인
                  $boardID = $_GET['boardID'];// $_GET['boardID']의 값을 변수 $boardID에 대입

                  //데이터 가져오기
                  $sql = "SELECT * FROM board WHERE boardID = {$boardID} ";
                  $result = $dbConnect->query($sql);

                  if ($result) {
                    $blogdetail = $result->fetch_array(MYSQLI_ASSOC);// MYSQLI_ASSOC : 컬럼명으로 값을 불러옴
                    // boardID, memberID, title, category, content, regTime, checked

                    ?>

            <h2 class="mb-3"><?php echo "{$blogdetail['title']}"; ?></h2>

            <p><?php echo "{$blogdetail['content']}"; ?></p>

            <div class="pt-5 mt-5">
          
            <?php if (isset($_SESSION['memberID'])){
               ?>
               <a href=<?php echo "'/board/editor/?boardID=$boardID'"; ?>>
                 <button type='button' class='btn btn-primary btn-sm' >
               수정하기 </button></a>&emsp;

               <button type='button' class='btn btn-danger btn-sm'
               onclick="javascript:deleteBtn('<?=$boardID?>')">
               삭제하기 </button>
                <?php
               }
                ?>

            </div>

          </div> <!-- .col-md-8 -->
          <div class="col-lg-4 sidebar ftco-animate bg-light py-md-5">
  

            <!-- 카테고리 -->
            <div class="sidebar-box ftco-animate">
              <div class="categories">
                <h3>Categories</h3>

                <li><a href="/blog/?category=dev#contents">개발
              
                </a></li>
                <li><a href="/blog/?category=trv#contents">여행
    
                </a></li>
                <li><a href="/blog/?category=daily#contents">일상

                </a></li>
              </div>
            </div>

          </div>
        </div>
      </div>
    </section> <!-- .section -->

    <?php
  } else {
    echo "잘못된 접근입니다.";
    exit;
  }
} else {
echo "잘못된 접근 입니다.";
exit;
}

?>
    <!-- 하단 -->
    <footer><?php include_once './include/footer.php'; ?></footer>




  <!-- loader -->
  <div id="ftco-loader" class="show fullscreen"><svg class="circular" width="48px" height="48px"><circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke="#eeeeee"/><circle class="path" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke-miterlimit="10" stroke="#F96D00"/></svg></div>

  <script src="js/jquery.min.js"></script>
  <script src="js/jquery-migrate-3.0.1.min.js"></script>
  <script src="js/popper.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/jquery.easing.1.3.js"></script>
  <script src="js/jquery.waypoints.min.js"></script>
  <script src="js/jquery.stellar.min.js"></script>
  <script src="js/owl.carousel.min.js"></script>
  <script src="js/jquery.magnific-popup.min.js"></script>
  <script src="js/aos.js"></script>
  <script src="js/jquery.animateNumber.min.js"></script>
  <script src="js/bootstrap-datepicker.js"></script>
  <script src="js/scrollax.min.js"></script>
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBVWaKrjvy3MaE7SQ74_uJiULgl1JY0H2s&sensor=false"></script>
  <script src="js/google-map.js"></script>
  <script src="js/main.js"></script>

<!-- 삭제버튼 클릭 시 -->
  <script> function deleteBtn(boardID){
    // alert('버튼이 클릭되었습니다');
    var deletalert = confirm('해당 게시물을 삭제 하시겠습니까?');
    if(deletalert == true){
      location.href='/board/editor/deleteBoard.php?boardID='+boardID;

    }
    else if(deletalert == false){

    }
    // location.href='/sovpdlwl/index.php';
  } </script>

  </body>
</html>

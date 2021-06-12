<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속
 ?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <?php include_once './include/head.php'; ?>
  </head>

  <body>
    <!-- 상단 메뉴바 -->
    <header><?php include_once './include/header.php'; ?></header>

<main>

      <!-- END nav     style="background-image: url('images/bg_1.jpg');"-->
      <section class="hero-wrap hero-wrap-2 js-fullheight" data-stellar-background-ratio="0.5">
        <div class="overlay"></div>
        <div class="container">
          <div class="row no-gutters slider-text js-fullheight align-items-end justify-content-center">
            <div class="col-md-9 ftco-animate pb-5 text-center">
              <h1 class="mb-3 bread">Blog</h1>
  <!--블로그 제목 하단 카테고리 태그 -->
              <p class="breadcrumbs">
                <span class="mr-2"><a href="?category=tot#contents">전체&ensp;<i class="ion-ios-arrow-forward"></i></a></span>&nbsp;
                <span class="mr-2"><a href="?category=dev#contents">개발&ensp;<i class="ion-ios-arrow-forward"></i></a></span>&nbsp;
                <span class="mr-2"><a href="?category=trv#contents">여행&ensp;<i class="ion-ios-arrow-forward"></i></a></span>&nbsp;
                <span class="mr-2"><a href="?category=daily#contents">일상&ensp;<i class="ion-ios-arrow-forward"></i></a></span>&nbsp;
              </p>
            </div>
          </div>
        </div>
      </section>

      <section class="ftco-section"  id="contents">
        <div class="container">
          <div class="row d-flex ">
            <?php
            // url에 있는 id pavge 값 있으면 가져옴
              if (isset($_GET['page'])) {
                $page = (int) $_GET['page'];
              }else {
                $page = 1;//없으면 1페이지
              }
              // 한번에 출력할 게시물 숫자
              $numView = 6;
              // $firstLimitValue : 쿼리문 LIMLT문의 첫번째 값으로 사용, 쪽수의 값에  따라 불러오는 데이터를 정함
              $firstLimitValue = ($numView * $page) - $numView;
                  //LIMIT 0,20 = (20 * 1)-20
                  //LIMIT 20,20 = (20 * 2)-20
                  //LIMIT 40,20 = (20 * 3)-20
                // echo "출력결과 = ".$_GET['category']."&".$_SESSION['email'];
              $sql = "SELECT  * FROM board ";
              //어드민 로그인 상태아니고, 카테고리 있을때
              if (!isset($_SESSION['memberID'])){
                $sql .= "WHERE checked = 'o' ";
                if (isset($_GET['category'])) {
                  switch ( $_GET['category']) {
                    case 'dev':
                        $sql .= "AND category = '개발' ";
                      break;
                    case 'trv':
                        $sql .= "AND category = '여행' ";
                      break;
                    case 'daily':
                        $sql .= "AND category = '일상' ";
                      break;
                  }
                }
              } else {
                if (isset($_GET['category'])) {
                  switch ( $_GET['category']) {
                    case 'dev':
                        $sql .= "WHERE category = '개발' ";
                      break;
                    case 'trv':
                        $sql .= "WHERE category = '여행' ";
                      break;
                    case 'daily':
                        $sql .= "WHERE category = '일상' ";
                      break;
                  }
                }
              }

              $sql .= "ORDER BY regTime ";
              $sql .= "DESC LIMIT {$firstLimitValue}, {$numView}"; //{$firstLimitValue}부터 {$numView}개 오름차순 출력
              $result = $dbConnect->query($sql);

              if ($result) {//게시글이 존재할 때
                  $dataCount = $result->num_rows;//게시물 데이터의 개수
                  // $dataCount = mysqli_num_rows($result);
                  if ($dataCount == 0) {
                    echo "게시글이 없습니다.";
                  }
                  else if ($dataCount > 0) {
                    for ($i=0; $i < $dataCount; $i++) {
                      $blogInfo = $result->fetch_array(MYSQLI_ASSOC);// MYSQLI_ASSOC : 컬럼명으로 값을 불러옴
                      // $num = $i+1;
                      // boardID, memberID, title, category, content, regTime, checked
                      $postYear = date('Y', $blogInfo['regTime']);
                      $postMonth = date('F', $blogInfo['regTime']);
                      $postDay = date('j', $blogInfo['regTime']);
                      ?>

                      <!-- 미리보기 -->
                      <div class="col-md-4 d-flex ftco-animate">
                      	<div class="blog-entry justify-content-end">
                          <a <?php echo "href='blog-single.php?boardID={$blogInfo['boardID']}#contents'"; ?>
                          class="block-20"  style="background-image: url('<?php echo $blogInfo['thumbnail'] ?>');">
                          </a>
                          <div class="text mt-3 float-right d-block">
                          	<div class="d-flex align-items-center mb-4 topp">
                          		<div class="one">
                          			<span class="day"><?php echo $postDay; ?></span>
                          		</div>
                          		<div class="two">
                          			<span class="yr"><?php echo $postYear; ?></span>
                          			<span class="mos"><?php echo $postMonth; ?></span>
                          		</div>
                          	</div>
                            <h3 class="heading"><a <?php echo "href='blog-single.php?boardID={$blogInfo['boardID']}#contents'>"; echo "{$blogInfo['title']}"; ?></a></h3>
                            <!-- 미리보기 사진 크기 맞추기 -->
                            <p>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;</p>
                          </div>
                        </div>
                      </div>

                  <?php  }

          }

        } else {
                // 데이터가 없을 경우
                echo "게시글이 없습니다.";
              }
             ?>

          </div>
        </div>

    <!-- 페이징 -->
        <div class="row mt-5">
          <div class="col text-center">
            <div class="block-27">
              <ul>
               <?php
               // 페이지가 없으면 페이징 없음
               include $_SERVER['DOCUMENT_ROOT'].'/blog/nextPage.php';//다음 페이지 이동(페이징)
                ?>
              </ul>
            </div>
          </div>
        </div>

      </section>


</main>

<!-- 하단 -->
<footer><?php include_once './include/footer.php'; ?></footer>



  <!-- loader -->
  <div id="ftco-loader" class="show fullscreen">
    <svg class="circular" width="48px" height="48px">
      <circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke="#eeeeee"/>
      <circle class="path" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke-miterlimit="10" stroke="#F96D00"/>
    </svg>
  </div>

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


  </body>
</html>

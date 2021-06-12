<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
include $_SERVER['DOCUMENT_ROOT'].'/common/permission.php';//접근 권한 확인
?>

<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <?php  include_once '../common/admin_head.php';?>
  <meta charset="utf-8">
    <title></title>
  </head>
  <body>

    <header><?php  include_once '../common/admin_header.php';?></header>
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
    <div class="testimony-wrap mr-5 ">
    <h3>연락처 목록</h3>
    </div>

    <div class="testimony-wrap  m-5 p-5">
      <table class="table table-striped text-center">

        <thead >
          <th>#</th>
          <th>이름</th>
          <th>이메일</th>
          <th>제목</th>
          <th>게시일</th>
          <th>답장</th>
        </thead>
        <tbody>

          <?php
          // url에 있는 id pavge 값 있으면 가져옴
            // if (isset($_GET['page'])) {
            //   $page = (int) $_GET['page'];
            // }else {
            //   $page = 1;//없으면 1페이지
            // }
            // // 한번에 출력할 게시물 숫자
            // // $numView = $nowPageCount;
            // $numView = 10;
            // // $firstLimitValue : 쿼리문 LIMLT문의 첫번째 값으로 사용, 쪽수의 값에  따라 불러오는 데이터를 정함
            // $firstLimitValue = ($numView * $page) - $numView;
            //     //LIMIT 0,20 = (20 * 1)-20
            //     //LIMIT 20,20 = (20 * 2)-20
            //     //LIMIT 40,20 = (20 * 3)-20
            //
            // //데이터 가져오기
            // $sql = "SELECT  * FROM touch ORDER BY regTime ";
            // $sql .= "DESC LIMIT {$firstLimitValue}, {$numView}"; //{$firstLimitValue}부터 {$numView}개 오름차순 출력
            // $result = $dbConnect->query($sql);
            //
            // if ($result) {//게시글이 존재할 때
            //     $dataCount = $result->num_rows;//게시물 데이터의 개수
            //
            //     if ($dataCount > 0) {
            //       for ($i=0; $i < $dataCount; $i++) {
            //         $memberInfo = $result->fetch_array(MYSQLI_ASSOC);// MYSQLI_ASSOC : 컬럼명으로 값을 불러옴
            //         // $num = $i+1;
            //
            //         echo "<tr>";
            //         echo "<td>".$memberInfo['boardID']."</td>";// #
            //         echo "<td>{$memberInfo['getName']}</td>"; // 이름
            //         echo "<td>{$memberInfo['getEmail']}</td>"; // 이메일
            //         echo "<td><a href='/board/view.php?boardID=";//게시물의 제목을 클릭하면 갈 수 있는 링크
            //         echo "{$memberInfo['boardID']}'>"; //메세지 내용(링크 걸려있음)
            //         echo $memberInfo['title'];//내용을 클릭하면 디테일한 내용을 볼 수 있음
            //         echo "</a></td>";
            //         // echo "<td>{$memberInfo['message']}</td>";//내용을 클릭하면 디테일한 내용을 볼 수 있음
            //         echo "<td>".date('Y-m-d H:i',$memberInfo['regTime'])."</td>";//받은 날짜
            //         echo "<td><a href='/board/view.php?boardID=";//게시물의 제목을 클릭하면 갈 수 있는 링크
            //         echo "{$memberInfo['boardID']}'>";
            //         if ($memberInfo['checkedMsg'] == '회신하기') {
            //             echo "<button type='button' class='btn btn-dark btn-sm' >";
            //         }
            //         else if ($memberInfo['checkedMsg'] == '차단') {
            //             echo "<button type='button' class='btn btn-danger btn-sm' >";
            //         }
            //         else {
            //           echo "<button type='button' class='btn btn-outline-dark btn-sm' >";
            //         }
            //
            //         echo "{$memberInfo['checkedMsg']}</button></a></td>";//답장여부 회신 완료 = disabled='disabled',btn-outline-dark: 전송완료 btn-success :  초록 btn-danger : 빨간 버튼(차단)
            //         echo "</tr>";
            //       }
            //     }
            //
            // } else {
            //   // colspan : 열 합치기
            //   echo "<tr><td colspan='6'>게시글이 없습니다.</td></tr>";
            // }

           ?>
        </tbody>
      </table>
      <?php
        // include $_SERVER['DOCUMENT_ROOT'].'/board/nextPage.php';//다음 페이지 이동(페이징)
        // include $_SERVER['DOCUMENT_ROOT'].'/board/searchForm.php';//검색어 페이지
       ?>

    </div>
  </body>
</html>

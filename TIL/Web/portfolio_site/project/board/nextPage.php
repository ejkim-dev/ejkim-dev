<div class="row mt-5">
  <div class="col text-center">
    <div class="block-27">
      <ul>

      <?php
        // 전체 레코드 수 구하기
        $sql = "SELECT count(boardID) FROM touch";// touch 쿼리의 레코드 수를 불러옴
        $result = $dbConnect->query($sql);//쿼리문 실행

        $boardTotalCount = $result->fetch_array(MYSQLI_ASSOC);// 쿼리문의 데이터를 변수 boardTotalCount에 대입
        $boardTotalCount = $boardTotalCount['count(boardID)'];// boardTotalCount 변수의 레코드 수 정보를 변수 boardTotalCount에 다시 대입

        //총 페이지 수, ceil은 올림을 하는 함수; 변수 $numView의 값인 20으로 페이지를 구성할 때 남는 게시물을 표시할때 올림 처리함
        $totalPage = ceil($boardTotalCount / $numView);

        // 처음 페이지 이동 링크 $_GET 방식을 사용하며, page의 값을 1로 적용
        echo "<li><a href='./list.php?page=1'>&lt;&lt;</a></li>";

        // 이전 페이지 이동 링크 : 처음 페이지인 1페이지에서는 이전 페이지 이동할 수 없으므로 이전 링크를 표시하지 않음
        if ($page != 1) {
          $previousPage = $page -1;
          echo "<li><a href='./list.php?page={$previousPage}'>&lt;</a></li>"; // 이전 링크를 출력
        }

        // 현재 페이지의 앞 뒤 페이지 수 표시
        $pageTerm = 5;

        // 처음 표시할 페이지를 현재 페이지를 기준으로 5개 이전까지만 표시
        $startPage = $page - $pageTerm;
        //음수일 경우 처리 : 음수 일 경우 $startPage에 1을 대입하여 1페이지부터 표시
        if ($startPage < 1) {
            $startPage = 1;
        }

        // 처음 표시할 페이지를 현재 페이지 기준으로 5개 이전까지만 표시
        $lastPage = $page + $pageTerm; // 현재 페이지에서 변수 $pageTerm의 값을 더하여 표시핧 페이지를 구함

        // 마지막 페이지의 수보다 클 경우 처리
        if ($lastPage >= $totalPage) {
          $lastPage = $totalPage;
        }

      // 표시할 페이지 링크에서 처음 시작할 페이지와 마지막 페이지를 구했으므로 이 값으로 for 반복문을 이용하여
      // 페이지 이동 링크를 생성
        for ($i=$startPage; $i <= $lastPage ; $i++) {
          $nowPageColor = 'unset';
          $nowBackgroundColor = 'unset';

          if ($i == $page) {
            $nowPageColor = '#fff';
            $nowBackgroundColor = '#33313b';
          }
          echo "<li><a href='./list.php?page={$i}'";
          echo "style='background-color:{$nowBackgroundColor} ;color:{$nowPageColor}'>{$i}</a></li>";
        }

        // 다음 페이지 이동 링크
        if ($page != $totalPage) {
            $nextPage = $page +1;
            echo "<li><a href='./list.php?page={$nextPage}'>&gt;</a></li>";

        }
        // 마지막 페이지 이동 링크
        echo "<li><a href='./list.php?page={$totalPage}'>&gt;&gt;</a></li>";
       ?>
     </ul>
   </div>
 </div>
</div>

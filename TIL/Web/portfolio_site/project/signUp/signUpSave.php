<?php
// 회원가입 폼에 입력된 데이터를 member 테이블에 저장
// 회원가입에 성공하면 바로 세션을 생성하기 위해 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속
  include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
  

  // form 태그로 받은 데이터를 모두 변수에 대입.
  // 생년월일은 정수형으로 형변환하여 숫자외 다른 값이 들어오면 값을 0으로 변경
  $email = $_POST['userEmail'];
  $nickName = $_POST['userNickName'];
  $pw = $_POST['userPw'];
  $birthYear = (int) $_POST['birthYear'];
  $birthMonth = (int) $_POST['birthMonth'];
  $birthDay = (int) $_POST['birthDay'];

// 전달받은 값이 적합하지 않을 때 사용할 함수
// 파라미터로 알림 문구를 받아 출력하는 기능과 회원가입 페이지로 이동하는 링크 태그를 출력
// a 태그는 링크 태그이며 href 속성에 이동할 주소를 입력
  function goSignUpPage($alert){
    echo $alert.'<br>';
    echo "<a href='./signUpForm.php'>회원가입 폼으로 이동</a>";
    return;
  }

  // 유효성 검사
  if (!filter_Var($email, FILTER_VALIDATE_EMAIL)) {
    // 이메일 검사 : 이베일 주소가 닉네임에 적합하지 않으면 goSignUpPage() 함수를 호출하고,
      goSignUpPage('올바른 이메일이 아닙니다.');
      // 페이지 작동을 중지
      exit;
  }

  // 비밀번호 검사 : 공백 체크
  if ($pw == null || $pw == '') {
    goSignUpPage('비밀번호를 입력해 주세요');
    exit;
  }
// 공백이 아니면 sha1 함수를 이용해 암호화
// sogha586 는 임의적으로 비밀번호에 값을 더하여 실제 입력한 값과 다르게 암호화함
  $pw = sha1('sogha586'.$pw);
  // $pw = hash('sha512', $pw);

  // 생년 검사
  if ($birthYear == 0) {
    goSignUpPage('생년을 정확히 입력해 주세요');
    exit;
  }

  // 생월 검사
  if ($birthMonth == 0) {
    goSignUpPage('생월을 정확히 입력해 주세요');
    exit;
  }

    // 생일 검사
    if ($birthDay == 0) {
      goSignUpPage('생일을 정확히 입력해 주세요');
      exit;
    }

    $birth = $birthYear.'-'.$birthMonth.'-'.$birthDay;//이상이 없으면 yyyy-mm-dd 형태로 값을 생성한 후 변수 birth에 대입

    //이메일 중복 검사
    $isEmailCheck = false;

    $sql = "SELECT email FROM member WHERE email = '{$email}'";
    $result = $dbConnect->query($sql);

    if ( $result ) {
        $count = $result->num_rows;
        if ($count == 0) {
          $isEmailCheck = true;//사용가능한 이메일
        } else {
          echo "이미 존재하는 이메일 입니다. ";
          goSignUpPage();
          exit;
        }
    } else {
      echo "에러발생 : 관리자 문의 요망";
      exit;
    }

    // 닉네임 중복 검사
    $isNickNameCheck = false;

    $sql = "SELECT nickName FROM member WHERE nickname = '{$nickName}'";
    $result = $dbConnect->query($sql);

    if ( $result ) {
      $count = $result->num_rows;
      if ($count == 0) {
        $isNickNameCheck = true;
      } else {
        goSignUpPage('이미 존재하는 닉네임 입니다.');
        exit;
      }
    } else {
      echo "에러발생 : 관리자 문의 요망";
      exit;
    }

    if ($isEmailCheck == true && $isNickNameCheck == true) {
        $regTime = time();
        $sql = "INSERT INTO member(email, nickname, pw, birthday, regTime)";
        $sql .= "VALUES('{$email}','{$nickName}','{$pw}',";
        $sql .= "'{$birth}', {$regTime})";
        $result = $dbConnect->query($sql);

        if ($result) {
          $_SESSION['memberID'] = $dbConnect->insert_id;
          $_SESSION['nickName'] = $nickName;
          Header("Location:../signIn/signInForm.php");
        } else {
          echo '회원가입 실패 - 관리자에게 문의';
          exit;
        }
    } else {
      goSignUpPage('이메일 또는 닉네임 중복입니다.');
      exit;
    }
 ?>

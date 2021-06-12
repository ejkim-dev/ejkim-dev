<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속
 ?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>editor</title>
    <link rel="stylesheet" href="/blog/css/style.css">
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <link href="summernote-lite.css" rel="stylesheet">
    <script src="summernote-lite.js"></script>
    <script src="lang/summernote-ko-KR.js"></script>
    <!-- <script src="https://github.com/summernote/summernote/tree/master/lang/summernote-ko-KR.js"></script> -->
    <script >
    // 모든 html 페이지가 화면에 뿌려지고 나서 ready안에 서술된 이벤트들이 동작 준비를 함
      $(document).ready(function() {
        // console.log("써머노트로그!!"); //  $('#summernote').summernote 안에다 로그를 쓰면 써머노트 동작 안함
        $('#summernote').summernote({
          lang: 'ko-KR', // default: 'en-US'ko-KR
          height: 400, //에디터높이
          minHeight : null, //최소 높이
          maxHeight : null, //최대 높
          focus: true, //default : true; 에디터 로딩후 포커스를 맞출지 여부
          // callbacks: {//callbacks 를 추가하면 에디터에 그림이 안뜸
          //   // console.log("써머노트로그 콜백"); //로그 찍으면 안됨!!
          //     onImageUpload : function(files, editor, welEditable) {
          //       console.log("써머노트로그 onImageUpload : function");
          //     sendFile(files[0], editor, welEditable);//단일 이미지 파일
          //     },
          //  },
            toolbar: [
                // [groupName, [list of button]]
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['font', ['strikethrough', 'superscript', 'subscript']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']],
                ['link',['link']],
                ['table',['table']],
                ['hr',['hr']],
                ['picture',['picture']]
            ]
          // callbacks: {
        	// 			onImageUpload: function(files, editor, welEditable) {
        	// 	            for (var i = files.length - 1; i >= 0; i--) {
        	// 	            	sendFile(files[i], this);
        	// 	            }
        	// 	        }
			    //        }
        });
        console.log("써머노트로그2");
      });
      /*이미지 파일 업로드*/
      function sendFile(file,editor,welEditable) {
          console.log("써머노트로그 sendFile");
       var data = new FormData();
       console.log("data : "+data);
       console.log("file : "+file);
       console.log("editor : "+editor);
       console.log("welEditable : "+welEditable);

       data.append("file", file);
       $.ajax({
           url: "saveimage.php",
           data: data,
           cache: false,
           contentType: false,
           processData: false,
           type: 'POST',
           success: function(data){
               var image = $('<img>').attr('src', '' + data); // 에디터에 img 태그로 저장을 하기 위함
              $('#summernote').summernote('editor.insertImage', url); // summernote 에디터에 img 태그를 보여줌
           },
           error: function(jqXHR, textStatus, errorThrown) {
               // console.log(textStatus+" "+errorThrown);//에러 로그 띄우기
               console.log(jqXHR.responseText);
           }
       });
      }
    </script>

  </head>
  <body>
    <!-- 상단 메뉴바 -->
    <header><?php include $_SERVER['DOCUMENT_ROOT'].'/common/adminHeader.php'; ?></header>

    <?php
// var_dump( $_SESSION );//세션 전체 값 불러오기
      if (isset($_GET['boardID']) && (int) $_GET['boardID'] > 0) // $_GET['boardID']가 존재하고 0을 초과하는지 확인
          $boardID = $_GET['boardID'];// $_GET['boardID']의 값을 변수 $boardID에 대입

        // echo "string".$boardID;

          //데이터 가져오기
          $sql = "SELECT * FROM board WHERE boardID = {$boardID}";
          $result = $dbConnect->query($sql);

          if ($result) {
            $blogedit = $result->fetch_array(MYSQLI_ASSOC);// MYSQLI_ASSOC : 컬럼명으로 값을 불러옴
            // boardID, memberID, title, category, content, regTime, checked
            $title = $blogedit['title'];
            $category = $blogedit['category'];
            $content = $blogedit['content'];
            $regTime = $blogedit['regTime'];
            $checked = $blogedit['checked'];
            $thumbnail = $blogedit['thumbnail'];
            // echo "출력 = {$title}";
          }
          ?>

  <div class="comment-form-wrap col-9 ">
    <form name="sendMail" id="sendMail"
    <?php
    if ($boardID > 0) { //$boardID 값이 0보다 크면  editblog.php에 boardID를 보냄
      echo "action='editblog.php?boardID={$blogedit['boardID']}'";
    } else {//$boardID값이 없으면 새 블로그 업로드로 넘어감
      echo "action='saveBoard.php'";}?> enctype="multipart/form-data" class="p-5 bg-light" method="post">
      <h4 class="mb-5">Blog Editor</h4>
      <div class="form-group">
        <a href='javascript:history.back()'><button type='button' class='btn btn-primary btn-sm' >
        뒤로가기 </button></a>&emsp;
        <button type='submit' class='btn btn-dark btn-sm' >
        저장하기 </button>&emsp;
      </div>
      <div class="form-group">
        <label for="text">제목&emsp;&emsp;&emsp;&nbsp;</label>
        <input type="text" class="form-control-sm col-5" name="title" id="title"
        <?php
        if ($boardID > 0) {
          echo "value = '$title'";
        }
         ?>
         required />
      </div>
      <div class="form-group">
        <label for="text">카테고리&emsp;&nbsp;</label>
        <select class="form-control-sm col-5" name="category" id="category" required />
        <?php
        if ($boardID > 0) {?>
          <option value='1' <?php if($category == "개발") echo "SELECTED";?>>개발</option>
          <option value='2' <?php if($category == "여행") echo "SELECTED";?>>여행</option>
          <option value='3' <?php if($category == "일상") echo "SELECTED";?>>일상</option>
      <?php  } else {
         ?>
        <option value='1' >개발</option>
        <option value='2'>여행</option>
        <option value='3'>일상</option>
      <?php } ?>
        </select>
      </div>
      <div class="form-group">
        <label for="text">공개여부&emsp;&nbsp;</label>
        <select class="form-control-sm col-5" name="public" id="public" required />

        <?php
        if ($boardID > 0) {?>
          <option value='o' <?php if($checked == "o") echo "SELECTED";?>>공개</option>
          <option value='x' <?php if($checked == "x") echo "SELECTED";?>>비공개</option>
      <?php  } else {
         ?>
         <option value='o' >공개</option>"
         <option value='x'>비공개</option>"
      <?php } ?>

        </select>
      </div>

      <!-- 미리보기 -->
      <div class="form-group">
        <label for="text"><?php if($boardID > 0){echo "미리보기 이미지 수정&nbsp;";} else {
          echo "미리보기 이미지&nbsp;";
        } ?></label>
        <!-- <form name = "fileUpload" action="./imageUpload.php" method="post"
              enctype="multipart/form-data"> -->

            <!-- 이미지 파일만 등록되게 accept 확장지 지정 -->
            <input class="inp-img" type="file" name="imgFile" id="imgFile" accept=".gif, .jpg, .png"/>
            <!-- <span class="btn-delete btn btn-danger btn-sm" type = "button">삭제</span> -->
            <div id="preview"></div>
            <!-- <input type="submit" value="업로드"/> -->
        <!-- </form> -->
        <!-- <div id="image_preview">
           <img src="#" />
           <br />
           <a href="#">Remove</a>
       </div> -->
       <!-- 수정하기 누르면 저장된 미리보기 이미지가 나타남 -->
       <?php
       // if ($boardID > 0) {
         // echo "현재 미리보기이미지 &emsp;";
         // echo "<img src='{$thumbnail}' width='100'/>";//저장된 파일 넓이 100으로 불러오기
         // echo "<input type='hidden' name='thumbnail' id='thumbnail' value='{$thumbnail}'>";
         // echo "$thumbnail";
       // }
        ?>

       <script type="text/javascript">

       // 등록 이미지 등록 미리보기
          function readInputFile(input) {
              if(input.files && input.files[0]) {
                  var reader = new FileReader();
                  reader.onload = function (e) {
                      // $('#preview').html("선택된 미리보기이미지 <img src="+ e.target.result +"  width='100'>");
                      $('#summernote').summernote('editor.insertImage',  e.target.result); // summernote 에디터에 img 태그를 보여줌
                  }
                  reader.readAsDataURL(input.files[0]);
              }
          }

          $(".inp-img").on('change', function(){
              readInputFile(this);
          });


          // 등록 이미지 삭제 ( input file reset )
          function resetInputFile($input, $preview) {
              var agent = navigator.userAgent.toLowerCase();
              if((navigator.appName == 'Netscape'
              && navigator.userAgent.search('Trident') != -1) ||
               (agent.indexOf("msie") != -1)) {
                  // ie 일때
                  $input.replaceWith($input.clone(true));
                  $preview.empty();
              } else {
                  //other
                  $input.val("");
                  $preview.empty();
              }
          }

          $(".btn-delete").click(function(event) {
              var $input = $(".inp-img");
              var $preview = $('#summernote');
              resetInputFile($input, $preview);
          });






        /**
        onchange event handler for the file input field.
        It emplements very basic validation using the file extension.
        If the filename passes validation it will show the image using it's blob URL
        and will hide the input field and show a delete button to allow the user to remove the image
        */

        // $('#imgFile').on('change', function() {
        //
        //     ext = $(this).val().split('.').pop().toLowerCase(); //확장자
        //
        //     //배열에 추출한 확장자가 존재하는지 체크
        //     if($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
        //         resetFormElement($(this)); //폼 초기화
        //         window.alert('이미지 파일이 아닙니다! (gif, png, jpg, jpeg 만 업로드 가능)');
        //     } else {
        //         file = $('#imgFile').prop("files")[0];
        //         blobURL = window.URL.createObjectURL(file);
        //         $('#image_preview img').attr('src', blobURL);
        //         $('#image_preview').slideDown(); //업로드한 이미지 미리보기
        //         $(this).slideUp(); //파일 양식 감춤
        //     }
        // });

        /**
        onclick event handler for the delete button.
        It removes the image, clears and unhides the file input field.
        */
        // $('#image_preview a').bind('click', function() {
        //     resetFormElement($('#imgFile')); //전달한 양식 초기화
        //     $('#imgFile').slideDown(); //파일 양식 보여줌
        //     $(this).parent().slideUp(); //미리 보기 영역 감춤
        //
        //     return false; //기본 이벤트 막음
        // });


        /**
        * 폼요소 초기화
        * Reset form element
        *
        * @param e jQuery object
        */
        // function resetFormElement(e) {
        //     e.wrap('<form>').closest('form').get(0).reset();
        //     //리셋하려는 폼양식 요소를 폼(<form>) 으로 감싸고 (wrap()) ,
        //     //요소를 감싸고 있는 가장 가까운 폼( closest('form')) 에서 Dom요소를 반환받고 ( get(0) ),
        //     //DOM에서 제공하는 초기화 메서드 reset()을 호출
        //     e.unwrap(); //감싼 <form> 태그를 제거
        // }


        </script>

      </div>


    글쓰기
    <br>
    <!-- <div id="summernote" ></div> -->
    <textarea id="summernote" name="summernote"  required >
      <?php
      if ($boardID > 0) {
        echo "$content";
      }
       ?>
    </textarea>

    <!-- <input type="submit" value="저장"> -->
    </form>
  </div>

  </body>
</html>

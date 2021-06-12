package com.example.maumalrim;

import android.util.Log;

import com.example.maumalrim.Common.StaticValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*TODO : 처음 챗봇로직 만들어서 사용했지만 지금은 왓슨API를 사용*/
/*쉐어드에 채팅내용 저장*/
/*Arraylist에 상담 카테고리와 내용을 받으면 상담사를 연결해준다.
* 1. 인사와 상담에 관한 안내를 한다.
* ㄴ안녕하세요, 챗봇 라미에요. 전 상담사를 소개시켜줄 수 있어요! 상담하고 싶은 주제가 있나요?
*   ㄴ없다/아니/모르겠다 : 아래와 같은 상담 주제가 있어요! (카테고리 : 대인관계, 정신건강, 학업/진로, 일탈/비행, SNS, 가정, 성격, 성, 근로)
*   ㄴelse : "상담 주제 : ~~" 가 맞으신가요?
*       ㄴ응/맞아/ㅇㅇ/오케이/네/맞아요 : 대략적인 상담 내용을 알 수 있을까요?
*       ㄴ else : 상담주제를 다시 적어주세요!
*           ㄴ그럼 상담사 연결을 도와드리겠습니다.
*              ㄴ*이때 어떻게 해야하지?
*
* 나중에 추가로 생각해야할 것
* 1. 챗봇 TCP로 하는 이유 : 통신이 끊어졌을때 상담사 추천을 해줄 수 없고 (클라이언트에서 인터넷 연결되어있는지 알수있나?)
* 2. 지금은 상담유형 카테고리 속 키워드 한개가 있는지만 확인할 수 있으면 1점 올라가는데 나중에는 해당 키워드가 몇개 있는지 추출하는 것 생각
* 3. 점수 순으로 sort. arraylist sort 메소드가 있넹! hashmap도 있나?
*/
public class Chatbot {
    private static final String TAG = "Chatbot";
    String uText;
    String nickname = "임시이름";//쉐어드에서 닉네임 가져오기

    public Chatbot(String uText) {
        this.uText = uText;
    }

    public String ChatbotText(){
        String chatbotText =  "'ㅁ'";

        String[] choices = {"대인 관계", "정신 건강", "학업/진로", "가족 관계", "성격", "근로", "기타"};
//        String[] uTextKeywords = {
//                "대인",  "성격", "학교생활", "친구관계", "학생", "친한", "친구", "동료", "초등", "학교", "중학교", "왕따", "문제", "욕", "잘못", "걱정", "무리", "혼자", "상담_그룹_1",//0~16 그룹1 : 학교와 교우관계
//                "년제","대학","전문대","시험", "고사","점수","정신","건강", "성적", "학업", "진로", "공부", "시험", "자퇴", "입학", "고등학생", "고등학교", "우울증", "스트레스", "꿈", "편입", "진로", "상담_그룹_2",//17~33 그룹2 : 학업과 진로 상담
//                "집","쌍둥이","누나","언니","형","부모","가족", "가출", "이혼", "경제", "돈", "오빠", "아빠", "엄마", "동생", "상담_그룹_3",//34~42 그룹3 : 가족관계 상담
//                "근로", "아르바이트", "시급", "사장", "근무", "회사", "최저시급", "월급", "상담_그룹_4",//43~50 그룹4 : 근로/직업 상담
//                "학교폭력", "SNS", "인터넷", "기타","일탈", "비행", "폭력","가해자","학폭", "상담_그룹_5",//그룹5 : 기타상담
//        };

        //arrKeywords에 uTextKeywords 키워드 값을 넣고, 각 그룹의 마지막 문자의 인덱스값을 중심으로 그룹점수에 반영하기 위해
        //예를들어 그룹1에서 "혼자"가 마지막 문자인데, 마지막 문자가 나오기 전까지 단어가 포함되어있으면 그룹1 점수가 올라감
//        ArrayList arrKeywords = new ArrayList();
//        for(int i = 0; i < uTextKeywords.length; i++){
//            arrKeywords.add(uTextKeywords[i]);
//        }
//        Log.i("arrKeywords", "arrKeywords 크기 : "+arrKeywords.size());

        if (uText.equals("다시")||uText.equals("리셋")){

            StaticValue.counselingReceipt = new ArrayList<>();
            chatbotText = "원하시는 상담 내용을 적어주세요!";
            return chatbotText;
        }


        Log.d(TAG, "ChatbotText: counselingReceipt 사이즈 "+ StaticValue.counselingReceipt.size());

        switch (StaticValue.counselingReceipt.size()){
            case 0 :
                Log.d(TAG, "ChatbotText: 들어오니?");

                if (uText.equals("없다")||uText.equals("몰라")||uText.equals("모르겠어")||uText.equals("없어")||uText.equals("아니")||uText.length() < 4){
                    chatbotText = "생각나는게 없으시면, 아래 [보기]키워드를 참고해서 입력해주세요!\n\n[보기]\n";
                    for (int i = 0; i < choices.length; i++){
                        if (i == choices.length-1){
                            chatbotText += choices[i]+" 상담";

                            break;
                        }
                        chatbotText += choices[i]+" 상담, ";
                    }
                }
                else {
                    //유저 텍스트 안에 키워드들로 주제를 분석해줌
                    /* 그룹1: 청소년의 학교와 교우관계 : "학교생활", "친구관계", "학생", "친한친구", "초등학교", "중학교", "왕따", "문제", "욕", "잘못", "걱정", "무리", "혼자"
                    그룹2: 청소년의 학업과 관련된 키워드 : "공부", "시험", "자퇴", "입학" 등으로 "고등학생", "고등학교", "우울증", "스트레스", "꿈", "편입", "진로",
                    그룹3: 청소년의 가족환경의 변화에 관련된 키워드 : "가출", "이혼", 경제, "돈", "오빠", "아빠", "엄마", "동생"
                    그룹4: 청소 년의 근로와 관련된 키워드 : "아르바이트", "시급", "사장", "근무", "회사", "최저시급", "월급"
                    그룹5: 기타 */
//                    int group_1 = 0, group_2 = 0, group_3 = 0, group_4 = 0, group_5 = 0;
//                    HashMap<String, Integer> groups = new HashMap<>();
//
//                    Log.i("arrKeywords", "상담_그룹_1 returned: " + arrKeywords.indexOf("상담_그룹_1"));
//                    Log.i("arrKeywords", "상담_그룹_2 returned: " + arrKeywords.indexOf("상담_그룹_2"));
//                    Log.i("arrKeywords", "상담_그룹_3 returned: " + arrKeywords.indexOf("상담_그룹_3"));
//                    Log.i("arrKeywords", "상담_그룹_4 returned: " + arrKeywords.indexOf("상담_그룹_4"));
//                    Log.i("arrKeywords", "상담_그룹_5 returned: " + arrKeywords.indexOf("상담_그룹_5"));
//                    Log.i("arrKeywords", "arrKeywords 크기 : "+arrKeywords.size());

                    //arrKeywords에 값이 잘 들어가있는지 확인함
//                    for (int i = 0; i < arrKeywords.size(); i++) {
//                        Log.i("arrKeywords", "arrKeywords "+i+": " + arrKeywords.get(i));
//                    }
                    /*1. 전달 받은 문장을 단어로 쪼갠다.
                    * 2. 쪼갠 단어가 arrKeywords 에 포함되어있는지 확인하고
                    * 3. 인덱스 번호를 추출해서 기준 그룹 인덱스 값보다 작으면 그 그룹의 int값 점수를 올린다.*/

 /*                   for (int i = 0 ; i < arrKeywords.size(); i++){

                        if (uText.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_1")){
                            group_1++;
                            groups.put("학교와 교우관계", group_1);
                            Log.i("arrKeywords", "상담_그룹_1 : "+arrKeywords.get(i).toString());
                            Log.i("arrKeywords", "상담_그룹_1 점수 : "+group_1);
                        }
                        else if (uText.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_2") && i > arrKeywords.indexOf("상담_그룹_1")){
                            group_2++;
                            groups.put("학업과 진로", group_2);
                            Log.i("arrKeywords", "상담_그룹_2 : "+arrKeywords.get(i).toString());
                            Log.i("arrKeywords", "상담_그룹_2 점수 : "+group_2);
                        }
                        else if (uText.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_3") && i > arrKeywords.indexOf("상담_그룹_2")){
                            group_3++;
                            groups.put("가족관계", group_3);
                            Log.i("arrKeywords", "상담_그룹_3 : "+arrKeywords.get(i).toString());
                            Log.i("arrKeywords", "상담_그룹_3 점수 : "+group_3);
                        }
                        else if (uText.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_4") && i > arrKeywords.indexOf("상담_그룹_3")){
                            group_4++;
                            groups.put("근로와 직업", group_4);
                            Log.i("arrKeywords", "상담_그룹_4 : "+arrKeywords.get(i).toString());
                            Log.i("arrKeywords", "상담_그룹_4 점수 : "+group_4);
                        }
                        else if (uText.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_5") && i > arrKeywords.indexOf("상담_그룹_4"))  {
                            group_5++;
                            groups.put("기타 상담", group_5);
                            Log.i("arrKeywords", "상담_그룹_5 : "+arrKeywords.get(i).toString());
                            Log.i("arrKeywords", "상담_그룹_5 점수 : "+group_5);
                        }
                    }

                    if ((group_1+group_2+group_3+group_4+group_5)==0) {
                        groups.put("기타 상담", 1);
                    }*/

                    //상담기록지 채워넣기
                    StaticValue.counselingReceipt.add(0,""+Category(uText));
                    StaticValue.counselingReceipt.add(1,""+uText);

                    /*생각해야할 채팅 로직*/
//                   최대 점수를 구한다음 최대 점수를 가진 key를 찾는다. 동점일 경우 여러개일 수 있고,
//                   전체 점수가 0일 경우 key가 없을 수도 있다.
//                    저장된 Hashmap에서 최대값을 구한 다음, 최대값과 같은 key들을 뽑아서 최대값인 키만 전달한다.

                    /*상담사에게 내용을 보내고, 상담사가 읽을때까지 */
                    //디버그용 : 추후 주석
//                    chatbotText = "결과를 볼까요?\n";

//                    Set<String> keys = groups.keySet();

//                    chatbotText += "상담 유형 : "+ groups+"\n";
//                    chatbotText += "group_1 = "+group_1+"\n";
//                    chatbotText += "group_2 = "+group_2+"\n";
//                    chatbotText += "group_3 = "+group_3+"\n";
//                    chatbotText += "group_4 = "+group_4+"\n";
//                    chatbotText += "group_5 = "+group_5+"\n";
//                    chatbotText += StaticValue.counselingReceipt.get(0)+"\n";
//                    chatbotText += StaticValue.counselingReceipt.get(1); //여기까지 상담사에게 보내기

                    chatbotText = "상담이 접수되었습니다. 상담사 연결까지 시간이 소요될 수 있습니다. \n\n'나가기'라고 입력해주시거나 왼쪽 상단 버튼을 눌러주세요.";
                }


                return chatbotText;

            default:
                chatbotText ="작성한 내용을 삭제합니다.\n"+StaticValue.counselingReceipt.get(1)
                +"\n원하는 상담 내용을 다시 작성해주세요.";

                //상담기록지 초기화
                StaticValue.counselingReceipt = new ArrayList<>();
                return chatbotText;
        }

    }

    public ArrayList<String> Category(String text){
        ArrayList<String> category = new ArrayList<>();

        String[] uTextKeywords = {
                "대인",  "성격", "학교생활", "친구관계", "학생", "친한", "친구", "동료", "초등", "학교", "중학교", "왕따", "문제", "욕", "잘못", "걱정", "무리", "혼자", "상담_그룹_1",//0~16 그룹1 : 학교와 교우관계
                "년제","대학","전문대","시험", "고사","점수","정신","건강", "성적", "학업", "진로", "공부", "시험", "자퇴", "입학", "고등학생", "고등학교", "우울증", "스트레스", "꿈", "편입", "진로", "상담_그룹_2",//17~33 그룹2 : 학업과 진로 상담
                "집","쌍둥이","누나","언니","형","부모","가족", "가출", "이혼", "경제", "돈", "오빠", "아빠", "엄마", "동생", "상담_그룹_3",//34~42 그룹3 : 가족관계 상담
                "근로", "아르바이트", "시급", "사장", "근무", "회사", "최저시급", "월급", "알바","상담_그룹_4",//43~50 그룹4 : 근로/직업 상담
                "학교폭력", "SNS", "인터넷", "기타","일탈", "비행", "폭력","가해자","학폭", "상담_그룹_5",//그룹5 : 기타상담
        };

        ArrayList arrKeywords = new ArrayList();
        for(int i = 0; i < uTextKeywords.length; i++){
            arrKeywords.add(uTextKeywords[i]);
        }
        Log.i("arrKeywords", "arrKeywords 크기 : "+arrKeywords.size());

        int group_1 = 0, group_2 = 0, group_3 = 0, group_4 = 0, group_5 = 0;
        HashMap<String, Integer> groups = new HashMap<>();

        Log.i("arrKeywords", "상담_그룹_1 returned: " + arrKeywords.indexOf("상담_그룹_1"));
        Log.i("arrKeywords", "상담_그룹_2 returned: " + arrKeywords.indexOf("상담_그룹_2"));
        Log.i("arrKeywords", "상담_그룹_3 returned: " + arrKeywords.indexOf("상담_그룹_3"));
        Log.i("arrKeywords", "상담_그룹_4 returned: " + arrKeywords.indexOf("상담_그룹_4"));
        Log.i("arrKeywords", "상담_그룹_5 returned: " + arrKeywords.indexOf("상담_그룹_5"));
        Log.i("arrKeywords", "arrKeywords 크기 : "+arrKeywords.size());

        for (int i = 0 ; i < arrKeywords.size(); i++){

            if (text.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_1")){
                group_1++;
                groups.put("학교와 교우관계", group_1);
                Log.i("arrKeywords", "상담_그룹_1 : "+arrKeywords.get(i).toString());
                Log.i("arrKeywords", "상담_그룹_1 점수 : "+group_1);
            }
            else if (text.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_2") && i > arrKeywords.indexOf("상담_그룹_1")){
                group_2++;
                groups.put("학업과 진로", group_2);
                Log.i("arrKeywords", "상담_그룹_2 : "+arrKeywords.get(i).toString());
                Log.i("arrKeywords", "상담_그룹_2 점수 : "+group_2);
            }
            else if (text.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_3") && i > arrKeywords.indexOf("상담_그룹_2")){
                group_3++;
                groups.put("가족관계", group_3);
                Log.i("arrKeywords", "상담_그룹_3 : "+arrKeywords.get(i).toString());
                Log.i("arrKeywords", "상담_그룹_3 점수 : "+group_3);
            }
            else if (text.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_4") && i > arrKeywords.indexOf("상담_그룹_3")){
                group_4++;
                groups.put("근로와 직업", group_4);
                Log.i("arrKeywords", "상담_그룹_4 : "+arrKeywords.get(i).toString());
                Log.i("arrKeywords", "상담_그룹_4 점수 : "+group_4);
            }
            else if (text.contains(arrKeywords.get(i).toString()) && i < arrKeywords.indexOf("상담_그룹_5") && i > arrKeywords.indexOf("상담_그룹_4"))  {
                group_5++;
                groups.put("기타 상담", group_5);
                Log.i("arrKeywords", "상담_그룹_5 : "+arrKeywords.get(i).toString());
                Log.i("arrKeywords", "상담_그룹_5 점수 : "+group_5);
            }
        }

        if ((group_1+group_2+group_3+group_4+group_5)==0) {
            groups.put("기타 상담", 1);
        }

        if (groups.size() > 1) {

            // value 내림차순으로 정렬하고, value가 같으면 key 오름차순으로 정렬
            List<Map.Entry<String, Integer>> list = new LinkedList<>(groups.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    int comparision = (o1.getValue() - o2.getValue()) * -1;
                    return comparision == 0 ? o1.getKey().compareTo(o2.getKey()) : comparision;
                }
            });

            // 순서유지를 위해 LinkedHashMap을 사용
            Map<String, Integer> sortedMap = new LinkedHashMap<>();
            for(Iterator<Map.Entry<String, Integer>> iter = list.iterator(); iter.hasNext();){
                Map.Entry<String, Integer> entry = iter.next();
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            System.out.println("sortedMap = "+sortedMap);


            int i = 0;

            for(String key : sortedMap.keySet()) {
                if(i<2) {
//                    System.out.println((i++)+"번째"+key +" ");
                    category.add(key);
                }else {
                    return category;
                }
                i++;
            }

        }else {
            for (String key : groups.keySet()){
                category.add(key);
            }
        }

        return category;
    }


}

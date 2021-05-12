# MovieApp

## 프로젝트 설명
네이버 영화 API를 통해 JSON 객체를 받아와 RecyclerView를 통해 화면으로 출력해주는 어플입니다. 이때 받아온 JSON 객체를 정제하여 CardViw와 RecyclerView를 통해 화면에 출력합니다.

그외에도 프로필 설정에서 다양한 기능 추가하였습니다.



## naver api 사용법


SeachFragment.java에 가서 아래 코드에서 clientId와 ClientSecret값을 naver api를 신청해서 수정하면 됩니다.
```
 
public class SearchFragment extends Fragment {
    final String clientId = "naver clientId";//애플리케이션 클라이언트 아이디값";
    final String clientSecret = "naver clientScret";//애플리케이션 클라이언트 비밀번호
}

```

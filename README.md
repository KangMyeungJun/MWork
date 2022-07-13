MWork
### 인원 및 일정

---

- 인원 : 1인 (개인)
- 일정 : 2022.06.29~2022.07.13

## 사용기술

---

- OS : mac12.4 (Monterey),ubuntu
- Server : Embedded Apache Tomcat9.0, AWS EC2
- Language : Java,Javascript,HTML5,CSS,ThymeLeaf
- DB : Oracle11g
- Framework/Flatform : SpringBoot,Hibernate,QueryDsl,Docker
- Tool : GitHub,IntelliJ,Jenkins,Maven

## 서버구성도

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b500e80f-9ab1-4f90-b75d-32e8d55f333d/Untitled.png)

## 프로젝트 목적

---

- CI/CD 자동배포 구축, JPA, QueryDsl 학습을 위한 토이프로젝트
- OAuth2.0 + JWT를 활용한 카카오/네이버 로그인 구현
- 게시글, 댓글 CRUD 구현

## ERD

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0dd3eb73-f599-40b8-b673-366050123dd8/Untitled.png)

## Trouble Shooting

---

### AWS EC2 Memory 부족

- Jenkins를 ec2에 설치 한 이후 지속적으로 ec2 서버가 다운되는 현상이 발생

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/534e09ba-1f7d-4783-a1d3-58af5dba8601/Untitled.png)

- Jenkins를 잘못 설치한 줄 착각하여 수번을 재설치 하였으나 해결되지 않음
- EC2 Free tier의 경우 메모리 1Gbyte 까지 지원하여 서버 메모리 자체가 부족한 상황

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/fcde55f2-bcd2-4b31-aaa5-f484971d1594/Untitled.png)

- RAM은 부족한 반면 하드디스크는 넉넉한 상황 Swap Memory를 활용

<aside>
💡 SwapMemory : 하드디스크의 일부 용량을 RAM 용량처럼 사용하는 방법

주의 : 하드디스크는 RAM에 비해 읽기/쓰기 능력이 현저히 떨어짐 성능상 문제 유의

</aside>

- Swap Memory 2Gbyte 추가 이후 서버가 다운되는일 없음

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/37b3fc9e-15fa-4332-8b0f-c3ee744dbcee/Untitled.png)

### GitHub

---

[GitHub - KangMyeungJun/MWork](https://github.com/KangMyeungJun/MWork)

# MWork

### 인원 및 일정

---

- 인원 : 1인 (개인)
- 일정 : 2022.06.29~2022.07.13

## 사용기술

---

- OS : mac12.4 (Monterey),ubuntu
- Server : Embedded Apache Tomcat9.0
- Language : Java,Javascript,HTML5,CSS,ThymeLeaf
- DB : Oracle11g
- Framework/Flatform : SpringBoot,Hibernate,QueryDsl,Docker
- Tool : GitHub,IntelliJ,Jenkins,Maven

## 서버구성도

---

![Untitled](https://user-images.githubusercontent.com/83402586/178683057-121f4dee-dfef-43e5-b7df-1888827d7d79.png)

## 프로젝트 목적

---

- CI/CD 자동배포 구축, JPA, QueryDsl 학습을 위한 토이프로젝트
- OAuth2.0 + JWT를 활용한 카카오/네이버 로그인 구현
- 게시글, 댓글 CRUD 구현

## ERD

---

![Untitled 1](https://user-images.githubusercontent.com/83402586/178683044-4aa5cb41-67a6-463c-9d1f-a71d7597a388.png)

## Trouble Shooting

---

### AWS EC2 Memory 부족

- Jenkins를 ec2에 설치 한 이후 지속적으로 ec2 서버가 다운되는 현상이 발생

![Untitled 2](https://user-images.githubusercontent.com/83402586/178683050-2c4d328b-c35d-4c24-8f1c-e024467746d7.png)

- Jenkins를 잘못 설치한 줄 착각하여 수번을 재설치 하였으나 해결되지 않음
- EC2 Free tier의 경우 메모리 1Gbyte 까지 지원하여 서버 메모리 자체가 부족한 상황

![Untitled 3](https://user-images.githubusercontent.com/83402586/178683054-e84dbe7d-21aa-45ff-9fcd-ed2047f7dc15.png)

- RAM은 부족한 반면 하드디스크는 넉넉한 상황 Swap Memory를 활용

<aside>
💡 SwapMemory : 하드디스크의 일부 용량을 RAM 용량처럼 사용하는 방법

주의 : 하드디스크는 RAM에 비해 읽기/쓰기 능력이 현저히 떨어짐 성능상 문제 유의

</aside>

- Swap Memory 2Gbyte 추가 이후 서버가 다운되는일 없음

![Untitled 4](https://user-images.githubusercontent.com/83402586/178683055-17ed9a1a-d6c6-456d-acec-5d6fe74dd44b.png)

### GitHub

---

[GitHub - KangMyeungJun/MWork](https://github.com/KangMyeungJun/MWork)

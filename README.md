# Convenience-POS-System

소프트웨어학과 개인 게임 개발 프로젝트, 디자인과와의 협업

## 프로젝트명  
shadow(그림자를 기반으로 한 캐릭터에서 게임명을 고안)

<br>

## 프로젝트 소개 
자바의 스윙, 스레드 및 액션/키 리스너를 이용하여 GUI기반의 게임을 개발

<br>

## 개발기간

실질적인 개발 기간 2023.09.28~2023.11.03
프로젝트의 시작 2023.01.13 군복무를 하며 프로젝트의 기반을 잡음. (협업 계획 회의 및 캐릭터 디자인&게임 기능 구현)

<br>


## 게임 줄거리

고대 빛의 마을의 다크라는 아이는 선천적으로 빛의 힘을 사용하지 못하였다. 이로 인해 따돌림을 받는다. 어느날, 용이 마을의 유물인 빛의 조각을 훔쳐가고 빛을 잃은 마을 사람들은 죽어간다. 유일하게 어둠 속에서 자유롭게 움직일 수 있었던 다크는 빛의 조각을 찾기위한 여정을 떠난다.

<br>
<br>

## 게임 구성도

1) GameMenu
<br>
![pos](MainMenu.png)

* 게임 시작 화면
* ActionListner를 이용한 버튼 이벤트로 다른 Frame 생성 및 현재 창 종료 (버튼의 이벤트 - 게임시작(DragonFrame)/ 게임방법(HowToPlay)/ 게임 종료)

<br>

2)HowToPlay
<br>
![pos](gameStory.png)
![pos](howToPlay1.png)
![pos](howToPlay2.png)

* 게임방법을 알려주는 창
* 이미지 순서대로 게임 스토리, 스테이지1(DragonFrame), 스테이지2()에 대한 설명이다.
* ActionListner를 이용한 버튼 이벤트로 페이지의 이동을 구현
* -> 현재 페이지를 변수에 저장(page) - 오른쪽 버튼을 누르면 page++ / 왼쪽 버튼을 누르면 page--를 한 후에 repaint()
* -> 화면에 이미지를 출력하기 전에 switch문을 통해 page에 따라 해당 세 이미지 중 하나를 출력
* 버튼에 이미지를 넣어 디자인(롤 오버 시에 버튼이 더 어두워짐)

<br>

3) DragonFrame
![pos](DragonFrame.png)

* 게임의 첫 번째 스테이지
* 잠든 용의 눈을 피해 던전에 입장 해야함.
* 쓰레드를 통해 애니메이션을 구현!!
* 용이 잠에서 깨는 시간 및 메테오가 떨어지는 확률을 Math.random()의 난수를 이용하여 조정
-> Math.random()*2 - 0~1의 난수를 생성 0이 생성되었을 경우, 메테오를 떨어트림(캐릭터의 약간 앞에 떨어트려서 뒤로만 피할 수 있게 설정)
* 키리스너를 이용하여 방향키 누를 시(keyPressed)에 이벤트를 발생(왼쪽 방향키 - leftFlag = true / 오른쪽 방향키 - rightFlag = true)
-> 쓰레드에서 두 플래그를 if문을 통해 확인한 후 leftFlag가 true이면 왼쪽으로 이동 rightFlag가 true이면 오른쪽으로 이동
-> 쓰레드가 불려진 횟수를 cnt에 저장 : cnt 짝수 - 걷는 이미지 / 홀수 - 서있는 이미지 (걷는 것을 구현)
->  캐릭터 왼쪽 이동 - 왼쪽 보는 이미지 / 오른쪽 이동 - 오른쪽 보는 이미지
* 방향키를 떼었을 경우(keyReleased), 두 플래그 모두 false (이동을 멈춤)
* 메테오가 캐릭터에 부딪혔을 때 게임 오버 시키기 위해서 사각형 겹치는지 여부를 확인하는 공식 사용(intersect)
-> if(x1 + w1 >= x2 && x1 <= x2 + w2 && y1 + h1 >= y2 && y1 <= y2 + h2)

3-1) DragonFrame GameOver  (gameOverFlag가 true이면 GameOver 진행)
* 용이 눈을 뜨고 있을 때 움직였거나 떨어지는 메테오에 맞으면 키리스너를 모두 return시키고 캐릭터의 머리 위에 !아이콘을 출력 & 용이 눈을 계속 뜨고 지켜보고 있음.
* gameOverCnt를 이용하여 일정 시간이 지났을 때 GameOver화면을 출력
-> DragonFrame 때와 같이 gameOverCnt에 따른 걷기 이미지 출력 달리는 것을 표현하기 위에 캐릭터 뒤에 흙먼지 dust.gif출력
-> 캐릭터의 뒤에 flyingDragon.gif 출력 (용이 쫓아오고 있음을 표현)
* gameOverCnt가 일정 수를 넘었을 때 replay & menu버튼을 출력(이미지 절대 배치)
-> 액션리스너를 통해 버튼 클릭 이벤트 구현 (Rectangle 클래스의 Contain(Point p)를 이용하여 내가 클릭한 좌표가 버튼의 구역 안에 있는지 확인)
-> replay 클릭 시, 캐릭터의 위치와 DragonFrame에 이용되는 변수들 초기화(gameOverFlag=false)

4) 
5) 

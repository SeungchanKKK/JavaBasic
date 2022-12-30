class Main {
    public static void main(String[] args) {
        // 최대 승객수, 현재 승객수, 요금 , 버스번호, 주유량,현재속도 순으로 입력하시오
        Bus bus1=new Bus();
        Bus bus2=new Bus();

        //소모한 가스량을 입력하세요
        bus1.gas_spend(70);

        //버스의 속력을넣어주세요
        bus1.speed_change(30);

        //버스의상태
        bus1.state_change();

        // 탑승한 승객 수를 입력하세요
        bus1.p_intake(28);

        System.out.println("==================================2번째 버스=============================");
        //소모한 가스량을 입력하세요
        bus2.gas_spend(92);

        //버스의 속력을넣어주세요
        bus2.speed_change(30);

        //버스의상태
        bus2.state_change();

        // 탑승한 승객 수를 입력하세요
        bus2.p_intake(31);


        System.out.println("===================택시 예제입니다=========================================================");

        //택시
        Taxi taxi1= new Taxi();
        Taxi taxi2= new Taxi();

        //소모한 가스량을 넣어주세요
        taxi1.gas_spend(70);

        //승객수를 입력하세요
        taxi1.passenger_in(1);

        //목적지를 입력하세요
        taxi1.destination("종로4가역");

        //목적지까지의 거리를 입력하세요
        taxi1.p_out(9);

        //총수입량 확인
        taxi1.incomecheck();

        System.out.println("=============1번택시다른손님을받았을때=============================");

        //소모한 가스량을 넣어주세요
        taxi1.gas_spend(10);

        //승객수를 입력하세요
        taxi1.passenger_in(2);

        //속력올리기
        taxi1.acceleration(40);

        //목적지를 입력하세요
        taxi1.destination("신세계백화점");

        //목적지까지의 거리를 입력하세요
        taxi1.p_out(12);

        //총수입량 확인
        taxi1.incomecheck();

        System.out.println("========================2번택시===============================");

        //소모한 가스량을 넣어주세요
        taxi2.gas_spend(100);

        //승객수를 입력하세요
        taxi2.passenger_in(1);

        //목적지를 입력하세요
        taxi2.destination("종로4가역");

        //목적지까지의 거리를 입력하세요
        taxi2.p_out(9);

        //총수입량 확인
        taxi2.incomecheck();

    }
}

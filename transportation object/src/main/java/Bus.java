class Bus {
    int passengers_cap;
    int passengers;
    int price;
    int bus_num;
    int gas;
    int speed;
    String state;

    public Bus(  ) {
        this.passengers_cap = 30;
        this.passengers = 0;
        this.price = 1000;
        this.bus_num = (int)(Math.random()*100);
        this.gas = 100;
        this.speed = 0;
        this.state = "운행";
    }

    public int gas_spend(int gas_spend){
        gas=gas-=gas_spend;
        System.out.println("주유량"+gas);
     return gas;
    }

    public String state_change() {
        if (gas ==0) {
            state = "차고지행";
        } else if (gas <= 10) {
            state ="차고지행";
            System.out.println("주유가필요합니다");
        }
        else {
            state="정상운행";
        }
        System.out.println(bus_num+"번버스 "+state+"!!    현재속력   "+speed);
        return state;
    }
    public int p_intake(int p_intake){
        this.passengers=passengers;
        if(state!="정상운행"){
            System.out.println("운행중이아닙니다");
        }
        else {
            if(passengers<passengers_cap){
                passengers += p_intake;
                if(passengers>=30){
                    passengers=30;
                    System.out.println("30명넘게는 탈수없습니다");
                }
            }
            else {
                System.out.println("더이상 탑승이 불가능합니다");
            }
        }
        int rest= passengers_cap-passengers;
        int income=passengers*price;
        System.out.println("탑승객수"+passengers+"    잔여좌석"+rest+"   요금확인"+income);
        return passengers;
    }

    public int speed_change(int accelerate){
        this.speed=speed;
        if(gas<10){
            System.out.println("주유량을 확인해주세요");
            state="차고지행";
        }
        else {
            speed+= accelerate;
            System.out.println("현재속도"+speed);
        }
        return speed;
    }


}
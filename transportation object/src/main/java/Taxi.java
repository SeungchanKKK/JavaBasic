public class Taxi {
    int taxi_num;
    int gas;
    int speed;
    int t_distance;
    int o_distance;
    int d_distance;
    int price;
    int d_price;
    int taxi_cap;

    int income;
    String state;

    public Taxi() {
        this.taxi_num = (int)(Math.random()*100);
        this.taxi_cap=4;
        this.gas = 100;
        this.speed = 0;
        this.o_distance = 3;
        this.d_distance = t_distance-3;
        this.price = 3000;
        this.d_price = 800;
        this.state = "일반";
        this.income=0;
    }
    public String gas_spend(int gas_spned){
        gas-=gas_spned;
        if(gas<=10){
            state="주유필요";
        }
        System.out.println(taxi_num+"번택시   "+state+"!!  주유상태"+gas);
        return state;
    }

    public String passenger_in(int passenger){
        if(passenger>=1) {
            if (state == "일반") {
                state="운행중";
                taxi_cap-=passenger;
                System.out.println("탑승객수"+passenger+"   잔여좌석"+taxi_cap+"   기본요금확인"+price);
            }
            else {
                System.out.println("운행중이아닙니다");;
            }
        }
        return state;
    }
    public String destination(String destination){
        if(state=="운행중") {
            System.out.println("목적지:" + destination);
        }
        return destination;
    }

    public int p_out(int t_distance){
        if(state=="운행중") {
            t_distance = t_distance + d_distance;
            price = price + d_price * t_distance;
            System.out.println("요금" + price + "  목적지 까지의 거리" + t_distance);
            taxi_cap=4;
            income+=price;
            state="일반";
            price=3000;
        }
        return price;
    }
    public int incomecheck(){
        System.out.println(taxi_num+"번택시"+"오늘의 총수입은"+income+"원입니다");
        return income;
    }

    public int acceleration(int acceleration){
        if(state=="운행중") {
            speed += acceleration;
            System.out.println("현재속도는"+speed);
        }
        System.out.println(state);
        return speed;
    }

}

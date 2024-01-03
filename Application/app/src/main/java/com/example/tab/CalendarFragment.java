package com.example.tab;


import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CalendarFragment extends Fragment {
    public CalendarView calendarView;
    private com.bluehomestudio.luckywheel.LuckyWheel luckyWheel;
    java.util.List<com.bluehomestudio.luckywheel.WheelItem> wheelItems ;

    String point;
    public CalendarFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        luckyWheel = v.findViewById(R.id.luck_wheel);
        TextView text = v.findViewById(R.id.text);
        Button start = v.findViewById(R.id.spin_btn);

        //점수판 데이터 생성
        generateWheelItems();

        String[] fortune = {"오늘의 운세 총운은 “실사구시” 입니다.\n" +
                "오늘은 왠지 시도 때도 없이 웃음만 나오는 하루입니다. 그동안 애쓰고 고생한 당신에게 보답이 있는 하루이니 편안한 마음 가짐으로 지금의 기쁨을 만끽하시기 바랍니다. 그토록 보고싶었던 사람을 우연하게 만나는 일도 생길 수 있으며 예상치 못한 주위의 도움을 받을 수도 있습니다. 자신이 한 행동에 대해 스스로 자부심 또한 느낄 수 있으며 자신감도 더욱 살아날 수 있는 날입니다. 과욕을 부리지 않고 자신의 뜻을 이루기 위해 노력했다면 그 노력한 행동에 대한 하늘의 선물을 받을 수 있는 날입니다.",
                "오늘의 운세 총운은 “관망자세” 입니다.\n" +
                        "자신감을 갖기 위해 노력만 하면 오늘 하루가 다 풀리는 운세이지만 마음이 안정되지 못하는 것은 자신감이 결여되어 있기 때문입니다. 초조하거나 불안감을 느낄수록 편안한 마음으로 자신을 다스릴 줄 아는 지혜가 필요합니다. 다른 사람과 비교를 하는 등의 행동으로 인해 자신감을 저하시킬 수 있으니 자기 자신에 대한 절대적인 신뢰가 밑바탕이 되어야 합니다. 또한 이럴때 일수록 땀을 흘리면서 움직여 보세요. 당신이 애써 노력한 만큼 하루 해가 지기전에 행운이 살짝 미소를 보일것입니다.",
                "오늘의 운세 총운은 “와신상담” 입니다.\n" +
                        "전반적인 운세는 보통 정도라고 해야 하겠습니다. 다만 평소와 다른 점이 있다면 실수를 특히 주의해야 한다는 점이지요. 사소한 실수라도 자신의 일에 있어서 안 좋은 결과로 나타날 수 있습니다. 자신이 하는 일에 있어서 다른 사람의 말에도 귀를 기울일 줄 아는 자세가 필요하며 대충 하기보다는 최선을 다하는 모습이 필요합니다. 특별히 좋은 일도 나쁜 일도 일어나지 않기 때문에 오히려 안정된 하루를 보낼 수 있습니다. 신중하면 신중할수록 당신의 하루는 멋지게 빛날 것이니 그리 해 보도록 하십시오." ,
                "오늘의 운세 총운은 “대략성공” 입니다.\n" +
                        "무수한 유혹의 손길이 다가옵니다. 그러나 그 유혹을 물리치고 본인의 신념을 지켜나가면 대박의 결과를 가져올 것입니다. 주위의 환경에 휩쓸려 자신이 가야할 방향을 잃게 된다면 유혹을 피하지 못한 것입니다. 자신의 의지를 지키고 그 유혹들 속에서 당당히 벗어날 수 있는 것은 모두 자신 스스로의 노력에 의거합니다. 상황을 피하는데 급급해 자신의 양심을 속이기보다는 자신의 소신을 지키는 것이 당신이 하고자 하는 일을 원만히 풀어나가고 당신에게 번성과 대박 행운을 안겨다 줄 수 있는 하루입니다.",
                "오늘의 운세 총운은 “호연지기” 입니다.\n" +
                        "작은 어려움도 헤쳐나간 경험은 당신에게 자신감을 심어줄 것입니다. 사소한 일이라고 하여 무시하거나 과소평가 해서는 안됩니다. 그 사소한 일로 인해서 자신을 더욱 크게 만들어줄 밑바탕이 되어 줄 수 있습니다. 또한 그 자신감으로 인해 더욱 빛나는 당신을 만날 수 있습니다. 오늘도 바로 그러한 날입니다. 초반의 작은 실수만 극복하세요. 자신과 관련된 모든 일에 조그만 주의를 기울여 사소한 사고라도 예방하는 것이 좋습니다. 바로 지금 대박행운이 당신을 기다리고 있습니다.",
                "오늘의 운세 총운은 “외유내강” 입니다.\n" +
                        "당신의 신념을 지키고 소신껏 일에 전념하는 당신에게 오늘 조상님이 당신에게 큰 행운을 주는 날입니다. 자신의 의지를 바탕으로 열심히 노력해서 기다리는 결과가 있다면 반드시 좋은 성과로 나타날 것입니다. 누구에게나 찾아오는 일이 아닌 상당히 경사스러운 일일 것이며 남들로부터 부러움과 시기를 동시에 받을 수 있습니다. 마음에 여유가 넘치고 자신의 발전 또한 이루어 질 수 있는 날입니다. 이 행운은 그동안 열심이 일한 노력의 대가이니 마음껏 누려셔도 되는 하루입니다.",
                "오늘의 운세 총운은 “우공이산” 입니다.\n" +
                        "당신이 베푼 덕으로 인해서 하향곡선이던 당신의 운이 되돌아 올 수 있는 하루입니다. 자신 혼자 세상을 사는 것이 아니기 때문에 다른 사람들과의 조화도 중요합니다. 역지사지의 마음으로 상대방에게 베풀었던 마음이 다시 자신을 향해 돌아오는 것을 느낄 수 있습니다. 좋은 운을 함께 가져오기 때문에 여러가지로 이득을 볼 수 있습니다. 행운이란 본인 스스로가 노력해서 만들 수도 있지만 다른 사람의 도움으로 찾아올 수도 있습니다. 당신의 행동으로 인해 행운이 당신을 향해 미소 지을 것입니다.",
                "오늘의 운세 총운은 “암중모색” 입니다.\n" +
                        "노력해서 이루지 못하는 것은 아무것도 없습니다. 다만, 얼마만큼의 노력을 기울였는가의 차이입니다. 자신이 생각했던 일이 뜻하는 대로 풀리지 않아 괴롭더라도 꾸준히 한걸음씩 내딛다 보면 자신의 성과에 대해 뒤돌아 보게 되는 기회를 얻을 수 있습니다. 남들로 하여금 찬사를 받을 수 있을 만큼의 성과일 수도 있으며 자신의 노력을 얼마만큼이나 기울이냐가 크게 작용할 수 있습니다. 오늘 하루는 당신이 할 수 있는 노력의 최선을 다하시기 바랍니다. 그 노력은 행운으로 보답 받을 것입니다.",
                "오늘의 운세 총운은 “계포일낙” 입니다.\n" +
                        "복잡하다고 노력을 포기한다면 그 일로 인해서 하루의 운이 잘 풀리지 않을 것입니다. 하나씩 하나씩 해결해 간다는 느낌으로 노력하고 진행시켜보세요. 인내심을 가지고 모든 일에 임한다면 자신이 포기하지 않았던 자신의 결정에 대해 감사하게 될 것입니다. 또한 자신의 노력이 헛되지 않게 적절한 계획을 짜는 것도 중요하게 작용합니다. 언제 이렇게 일을 다 해결했을까 하는 생각이 들 정도로 작은 성과를 이룰 것이고 그 일이 당신에게는 내일을 웃으면서 밝게 볼 수 있을 것입니다.",
                "오늘의 운세 총운은 “태평성대” 입니다.\n" +
                        "오늘은 뒤로 넘어져도 돈을 줍는 하루입니다. 원하는 만큼의 목표를 달성할 수 있고 뜻하는 바를 모두 이룰 수 있기 때문에 자신 스스로 자부심을 느낄 수 있습니다. 또한 자신의 능력 이상의 일에 도전하는 경우에도 성공할 것입니다. 모든 상황이 자신에게 유리하도록 돌아갑니다. 살다가 오늘 처럼 행운이 앞서는 날도 드문 날이니 당신의 행운의 마지막은 어디까지 인지 한 번 확인해 보는 것도 중요합니다. 단 저녁에는 일찍 귀가하는게 유리합니다. 자신의 마음을 가다듬고 편안히 행운을 맞이하는 것도 즐거운 방법입니다.",
                "오늘의 운세 총운은 “중과부적” 입니다.\n" +
                        "당신이 어떻게 하고자 하는 마음이 오늘 당신의 운세를 조정하는 가장 중요한 요인입니다. 사람에게 있어 아무리 힘든 상황이 있어서 그 상황을 이겨내고자 하는 의지가 있다면 무엇도 장애가 될 수 없습니다. 어려운 일이 있다고 미리부터 실망하거나 포기하지 마세요. 당신이 얼마나 자신감을 가지고 노력하느냐에 따라 당신의 운세가 달라질 수 있습니다. 천천히 상황을 파악하고 조금 여유로운 마음으로 생활해 보세요. 지금의 어려움을 반드시 보상답게 되는 날이 생기니 용기를 가지세요.",
                "오늘의 운세 총운은 “이럴수가” 입니다.\n" +
                        "당신이 어떻게 하고자 하는 마음이 오늘 당신의 운세를 조정하는 가장 중요한 요인입니다. 이겨내려고 하는 의지를 기본으로 가지고 있다면 당신은 반드시 그 어려움으로부터 탈출할 수 있습니다. 또한 당신이 가는 길에 대해 확신한 소신을 가지고 움직이시기 바랍니다. 당신이 가는 방향에 당신의 운이 달려있을 수 있습니다. 또한 어려운 일이 있다고 실망하거나 포기하지 마세요. 당신의 노력으로 이겨낼 수 있습니다. 행동하는 의지만 있다면 능히 벗어날 수 있는 하루입니다.",
                "오늘의 운세 총운은 “기운만땅” 입니다.\n" +
                        "오늘 당신에게 특별하게 안 좋은 일이 생기거나 하지는 않습니다. 그냥 평상시와 비슷한 하루가 이어질 것처럼 보입니다. 그로 인해 마음도 고요하거나 편안한 상태를 유지할 수 있습니다. 너무나 단조로움을 느낄 수 있지만 순조롭다는 것은 그만큼 긍정적인 좋은 운의 흐름인 것입니다. 또한 오후 들어서는 행운도 따르니 기분도 상쾌해 질것입니다. 자신에게 놓여있는 상황을 천천히 지켜보는 여유를 가지고 다가오는 행운의 끈을 놓치지 않도록 하세요. 또 알아요 횡재수가 생길지?",
                "오늘의 운세 총운은 “일장일단” 입니다.\n" +
                        "오늘처럼 목표를 위해 노력하는 하루가 당신의 꿈을 이룰 수 있도록 해줄 것입니다. 마음에 여유를 가지고 행동하는 것이 가장 필요한 때입니다. 급할수록 돌아가라는 말이 있습니다. 목표만 생각하고 아무런 생각없이 앞서 나간다면 조금도 당신에게 이로울 것이 없는 것입니다. 또한 조급하게 생각하지 말고 때를 기다리세요. 아직은 노력하는 자세가 필요하지만 오늘의 노력은 조만간 큰 행운으로 보상 받게 될것입니다. 당신의 꿈을 위해 한걸음씩 다가서는 자세를 갖도록 하세요.",
                "오늘의 운세 총운은 “안전모드” 입니다.\n" +
                        "조금의 불편함을 감수하세요. 오늘은 왠지 잘 맞지 않은 옷을 입은 것처럼 불편한 날입니다. 이런날도 있고 저런날도 있다는 것을 순수하게 받아들이세요. 무슨 일이든 자신이 좋다고 하는 일이 항상 이루어질 수는 없는 것입니다. 마음을 좀 더 편안하고 여유롭게 가지면 자신이 하는 일을 좀 더 잘 마무리 할 수 있을 것입니다. 또한 어떤 일을 할 때도 착착 맞는 느낌이 아니라 뭐가 자꾸 어긋나는 느낌이 들 수 있으니 오늘만큼은 운의 흐름에 따라 순조롭게 처신하는 것이 좋습니다. 나쁜날이 아니니 크게 개의치 않으셔도 되겠습니다.",
                "오늘의 운세 총운은 “얼씨구야” 입니다.\n" +
                        "오늘은 주변 사람들의 도움으로 일이 순조롭게 풀리는 날입니다. 혼자서 하기에 벅찬 일이 있다면 편한 마음으로 주변에 도움을 요청하세요. 아마 당신을 진심으로 생각하는 사람들로부터 좋은 조언이나 도움을 받게 될 수 있습니다. 또한 당신이 오늘 하루를 지냄에 있어서 큰 어려움은 없습니다. 다만 작은 일부터 꼼꼼히 챙기는 센스를 보여주시기 바랍니다. 또한 당신이 곤란한 일이 있을 때, 어려움에 처해 있을 때마다 누군가의 도움으로 별 어려움 없이 순조롭게 풀리는 날입니다.",
                "오늘의 운세 총운은 “금의환향” 입니다.\n" +
                        "차곡차곡 쌓아둔 적금을 타는 것처럼 큰 뜻이 이루어지는 날입니다. 행복하고 기쁜 오늘입니다. 자신이 하고 있던 일에 대해서 좋은 성과로 그 기쁨을 나타낼 수 있으며 당신이 가는 그 길에 당신의 좋은 사람을 만나거나 좋은 인상을 줄 수 있는 자신의 능력이 발휘될 수 있습니다. 주변 사람들로부터 인정도 받게 되고 자신이 능력도 더욱 발전시킬 수 있으니 여러모로 행복한 하루가 될 것입니다. 그동안 힘들어도 꾸준히 열심히 노력하며 살아온 당신에게 결실을 맺는 날입니다.",
                "01월 02일의 운세 총운은 “노심초사” 입니다.\n" +
                        "매사가 권태로워 지는 날입니다. 이런 날일수록 늘어지는 기분을 잘 추슬러야 합니다. 자신의 기분이 날카롭기 때문에 다른 사람과의 관계에서 안 좋은 행동을 하기 쉽습니다. 자신이 한만큼 자신에게 돌아오기 때문에 사소한 배려를 갖는 마음가짐이 필요한 때입니다. 이 때를 빨리 탈피하려면 새로운 계획을 세우고 신속하게 움직이는 것이 좋습니다. 스스로를 자꾸 바쁘게 만들어주세요. 여러 가지를 생각하지 않기 때문에 오히려 가볍게 일을 해결 할 수 있는 기회를 만들기 쉬워집니다.",
                "오늘의 운세 총운은 “일보후퇴” 입니다.\n" +
                        "순탄하지 않은 것처럼 보이지만 정작 일을 마주한 후에는 운이 당신 쪽으로 따르기에 원만한 해결과 진행이 가능한 날입니다. 눈 앞에 작은 장애가 나타나서 짜증이나 스트레스를 받기 쉬울 수 있습니다. 언제나처럼 마음을 새롭게 다지고 주어진 상황을 천천히 둘러볼 필요성이 있습니다. 장애물에 지레 겁먹어서 맞서 보기도 전에 포기하는 일만 없다면 다 좋은 날이지요. 자신감을 가지고 행동하는 것과 상황을 피하지 않고 맞서 그 운을 자신의 편으로 만들기에 충분한 날입니다.",
                "오늘의 운세 총운은 “사고뭉치” 입니다.\n" +
                        "운이 따르는 점에 있어서는 다소 부족한 부분이 있기도 하지만 당신의 노력과 의지로 얼마든지 채워갈 수 있는 날입니다. 무슨 일을 하든 생각하기 나름이라는 태도를 가져보세요. 힘들게 느껴졌던 일도 깔끔하게 정리할 수 있는 기분을 느낄 수 있습니다. 또한 주변의 상황이 자신에게 나쁘지 않기 때문에 스스로 노력만 한다면 인정을 받을 수도 있으며 스스로도 자신감을 채울 수 있는 하루가 될 수 있습니다. 한번쯤은 무리수를 두어서 모험을 해보는 것도 그리 나쁘지 만은 않을 듯 하군요."};

        //점수판 타겟 정해지면 이벤트 발생
        luckyWheel.setLuckyWheelReachTheTarget(new com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                //아이템 텍스트 변수에 담기
                int e = (int) Math.floor(Math.random()*20);
                luckyWheel.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                text.setText(fortune[e]);
                start.setText("다시하기");

                //메시지
            }
        });

        //시작버튼

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.INVISIBLE);
                text.setVisibility(View.INVISIBLE);
                luckyWheel.setVisibility(View.VISIBLE);
                java.util.Random random = new java.util.Random();
                point = String.valueOf(random.nextInt(6)+1); // 1 ~ 6
                luckyWheel.rotateWheelTo(Integer.parseInt(point));
            }
        });
        calendarView = v.findViewById(R.id.calendarView);
        // Inflate the layout for this fragment
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                android.content.Intent intent = new Intent(getActivity(), CalendarDate.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                String prev_class = "asd";
                intent.putExtra("prev_class", prev_class);
                startActivity(intent);
            }
        });

        return v;
    }

    private void generateWheelItems() {

        wheelItems = new java.util.ArrayList<>();


        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#F44336"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#E91E63"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#9C27B0"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#3F51B5"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#1E88E5"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#F44336"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#E91E63"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#9C27B0"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#3F51B5"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#1E88E5"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));
        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#F44336"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#E91E63"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#9C27B0"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#3F51B5"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#1E88E5"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));
        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#F44336"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#E91E63"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#9C27B0"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#3F51B5"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));

        wheelItems.add(new com.bluehomestudio.luckywheel.WheelItem(Color.parseColor("#1E88E5"), BitmapFactory.decodeResource(getResources(),
                R.drawable.qw), " "));
        //점수판에 데이터 넣기
        luckyWheel.addWheelItems(wheelItems);
    }


    /**
     * drawable -> bitmap
     * @param drawable drawable
     * @return
     */
    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
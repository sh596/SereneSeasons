package sereneseasons.bossbar;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BossBar {

    private static final Logger log = LoggerFactory.getLogger(BossBar.class);
    private static ServerBossEvent bossBar;

    //보스바 업데이트(world tick)
    public static void updateBossBar(float progress, float savedData){
        if(bossBar != null){
            progress = Float.parseFloat(String.format("%.1f", progress));
            bossBar.setProgress(progress); //틱 당 보스바 값 업데이트
            bossBar.setName(Component.literal("온도: " + savedData)); //틱당 온도 표시 변경
            if(savedData < 30.0f){
                bossBar.setColor(BossBarColor.BLUE);
            }
            else if(savedData > 40.0f){
                bossBar.setColor(BossBarColor.RED);
            }
        }
    }

    //보스바 객체 생성(start tick)
    public static void showBossBar(ServerPlayer player) {

        Component bossName = Component.literal("온도: 36.5");

        bossBar = new ServerBossEvent(bossName, BossBarColor.PINK, BossBarOverlay.PROGRESS); //보스바 객체 생성
        if (bossBar != null) {

            bossBar.setProgress(0.5F); //보스바 초기값 설정
            bossBar.addPlayer(player); //플레이어 접속 확인
            log.info("BossBar");
        }else {
            log.info("BossBar is null!");
        }

    }
}
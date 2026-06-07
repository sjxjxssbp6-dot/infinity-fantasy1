package cupcake.nikky.imperium.tower.donate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public enum SubscriptionDurationType {

    NONE(0),
    WEEK_2(TimeUnit.DAYS.toMillis(14)),
    WEEK_4(TimeUnit.DAYS.toMillis(30)),
    MONTH_2(TimeUnit.DAYS.toMillis(60)),
    MONTH_6(TimeUnit.DAYS.toMillis(180)),
    MONTH_12(TimeUnit.DAYS.toMillis(365));

    @Getter
    private final long duration;

}

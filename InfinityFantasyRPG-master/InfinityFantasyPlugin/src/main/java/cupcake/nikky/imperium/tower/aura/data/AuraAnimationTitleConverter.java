package cupcake.nikky.imperium.tower.aura.data;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.wada.module.aura.AuraAnimation;

@UtilityClass
public class AuraAnimationTitleConverter {

    public String convert(@Nullable final AuraAnimation animation) {
        return switch (animation) {
            case NONE -> "Отсутствует";
            case WAVE -> "Волна";
            case ARROW -> "Стрелка";
            case PULSE_ROUND -> "Круглая пульсация";
            case DOUBLE_CIRCLE -> "Двойной круг";
            case PULSE_HORIZONTAL -> "Горизонтальная пульсация";
            case PULSE_ROUND_REVERSE -> "Обратная круговая пульсация";
            case PULSE_HORIZONTAL_REVERSE -> "Обратная горизонтальная пульсация";
            case RANDOM_UP -> "Случайная";
            case null, default -> "Отсутствует";
        };
    }

}

package cupcake.nikky.imperium.tower.entity.monster.goal.navigation;

import net.minecraft.server.v1_12_R1.*;

public class FantasyNavigation extends Navigation {

    public FantasyNavigation(EntityInsentient var1, World var2) {
        super(var1, var2);
    }

    @Override
    public boolean a(int var1, int var2, int var3, int var4, int var5, int var6, Vec3D var7, double var8, double var10) {
        int var12 = var1 - var4 / 2;
        int var13 = var3 - var6 / 2;
        if (!this.b(var12, var2, var13, var4, var5, var6, var7, var8, var10)) {
            return false;
        } else {
            for(int var14 = var12; var14 < var12 + var4; ++var14) {
                for(int var15 = var13; var15 < var13 + var6; ++var15) {
                    double var16 = var14 + 0.5 - var7.x;
                    double var18 = var15 + 0.5 - var7.z;
                    if (var16 * var8 + var18 * var10 >= 0.0) {
                        var pathType = this.h.a(this.b, var14, var2 - 1, var15, this.a, var4, var5, var6, true, true);
                        if (pathType == PathType.WATER || pathType == PathType.LAVA) {
                            return false;
                        }

                        pathType = this.h.a(this.b, var14, var2, var15, this.a, var4, var5, var6, true, true);
                        float var21 = this.a.a(pathType);
                        if (var21 < 0.0F || var21 >= 8.0F) {
                            return false;
                        }

                        if (pathType == PathType.DAMAGE_FIRE || pathType == PathType.DANGER_FIRE || pathType == PathType.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }
}

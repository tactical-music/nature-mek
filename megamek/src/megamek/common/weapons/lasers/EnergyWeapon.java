/**
 * MegaMek - Copyright (C) 2004,2005 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
/*
 * Created on May 29, 2004
 *
 */
package megamek.common.weapons.lasers;

import megamek.common.Game;
import megamek.common.ToHitData;
import megamek.common.WeaponType;
import megamek.common.actions.WeaponAttackAction;
import megamek.common.options.GameOptions;
import megamek.common.options.OptionsConstants;
import megamek.common.weapons.AttackHandler;
import megamek.common.weapons.EnergyWeaponHandler;
import megamek.common.weapons.Weapon;
import megamek.server.Server;

/**
 * @author Andrew Hunter
 */
public abstract class EnergyWeapon extends Weapon {

    /**
     *
     */
    private static final long serialVersionUID = 3128205629152612073L;

    public EnergyWeapon() {
        flags = flags.or(F_MECH_WEAPON).or(F_TANK_WEAPON).or(F_AERO_WEAPON).or(F_PROTO_WEAPON)
                .or(F_ENERGY);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * megamek.common.weapons.Weapon#getCorrectHandler(megamek.common.ToHitData,
     * megamek.common.actions.WeaponAttackAction, megamek.common.Game,
     * megamek.server.Server)
     */
    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit,
            WeaponAttackAction waa, Game game, Server server) {
        return new EnergyWeaponHandler(toHit, waa, game, server);
    }
    
    @Override
    public void adaptToGameOptions(GameOptions gOp) {
        super.adaptToGameOptions(gOp);

        // Add modes for dialed-down damage according to TacOps, p.102
        // Adds a mode for each damage value down to zero; zero is included
        // as it is specifically mentioned in TacOps.
        // The bombast laser has its own rules with to-hit modifiers and does not
        // get additional dial-down
        if (!(this instanceof ISBombastLaser)) {
            if (gOp.booleanOption(OptionsConstants.ADVCOMBAT_TACOPS_ENERGY_WEAPONS)) {
                int dmg = (damage == WeaponType.DAMAGE_VARIABLE) ? damageShort : damage;
                for (; dmg >= 0; dmg--) {
                    addMode("Damage " + dmg);
                }
            } else {
                int dmg = (damage == WeaponType.DAMAGE_VARIABLE) ? damageShort : damage;
                for (; dmg >= 0; dmg--) {
                    removeMode("Damage " + dmg);
                } 
            }
        }
    }
}

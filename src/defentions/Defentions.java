package defentions;

import models.cards.minion.ForceType;
import models.cards.minion.SideType;
import models.cards.*;
import models.cards.buff.Buff;
import models.cards.buff.BuffType;
import models.cards.spell.TargetForm;
import models.cards.spell.effect.Effect;
import models.cards.spell.effect.HouseEffect;
import models.cards.spell.effect.HouseEffectType;
import models.cards.hero.Hero;
import models.cards.hero.HeroSpecialItemPack;
import models.cards.minion.*;
import models.cards.spell.Spell;
import models.deck.Deck;
import models.item.Item;
import models.item.ItemActivateTime;
import models.item.ItemType;

import java.util.ArrayList;

public class Defentions {
    public static ArrayList<Spell> defineSpell() {
        ArrayList<Spell> spellCards = new ArrayList<>();


        {
            TargetForm targetForm = new TargetForm(0, 0, 1, 1, SideType.enemy, ForceType.both,null, true);
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.enemy,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,0, BuffType.disarm,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            Spell spell = new Spell(101, "TotalDisarm", 0, 1000, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/FreezeCard.png");
            spellCards.add(spell);
        }//1

        {
            TargetForm targetForm1 = new TargetForm(0, 0, 2, 2, SideType.enemy, ForceType.both,null, true);
            ArrayList<Buff> buffs1=new ArrayList<>();
            buffs1.add(new Buff(0,0,0,BuffType.fail_positive_buff,false));
            Effect effect1=new Effect(buffs1,null,targetForm1);
            TargetForm targetForm2 = new TargetForm(0, 0, 2, 2, SideType.insider, ForceType.both,null, true);
            ArrayList<Buff> buffs2=new ArrayList<>();
            buffs1.add(new Buff(0,0,0,BuffType.fail_negative_buffs,false));
            Effect effect2=new Effect(buffs1,null,targetForm1);
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(effect1);
            effects.add(effect2);
            Spell spell = new Spell(102, "AreaDispel", 2, 1500, effects, null);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/FreezeCard.png");
            spellCards.add(spell);
        }//2

        {//3
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.insider,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,2,BuffType.attack_power_up,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(103, "Empower", 1, 250, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/LightningCard.png");
            spellCards.add(spell);
        }//3

        {//4
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.enemy,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,4,BuffType.hurt,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(104, "FireBall", 1, 400, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/LightningCard.png");
            spellCards.add(spell);
        }//4

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.insider,ForceType.hero,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,4,BuffType.attack_power_up,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(105, "GodStrength", 2, 450, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/LightningCard.png");
            spellCards.add(spell);
        }//5

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.insider,ForceType.hero,null);
            ArrayList<HouseEffect> houseEffects=new ArrayList<>();
            houseEffects.add(new HouseEffect(HouseEffectType.fire,1,2,0));
            TargetForm targetForm=new TargetForm(0,0,2,2,SideType.both,ForceType.allCell,null,true);
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(null,houseEffects,targetForm));
            Spell spell = new Spell(106, "HellFire", 3, 600, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Rocket.png");
            spellCards.add(spell);
        }//6

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.enemy,ForceType.hero,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,8,BuffType.hurt,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(107, "LightingBolt", 2, 1250, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/LightningCard.png");
            spellCards.add(spell);
        }//7

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.insider,ForceType.hero,null);
            ArrayList<HouseEffect> houseEffects=new ArrayList<>();
            houseEffects.add(new HouseEffect(HouseEffectType.poison,1,1,0));
            TargetForm targetForm=new TargetForm(0,0,3,3,SideType.both,ForceType.allCell,null,true);
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(null,houseEffects,targetForm));
            Spell spell = new Spell(108, "PoisonLake", 5, 900, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Poison_Spell_info.png");
            spellCards.add(spell);
        }//8

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.insider,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,3,4,BuffType.attack_power_up,false));
            buffs.add(new Buff(0,1,0,BuffType.disarm,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(109, "Madness", 0, 650, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/image.png");
            spellCards.add(spell);
        }//9

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,0,BuffType.disarm,false));
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.enemy,ForceType.both,null,true);
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            Spell spell = new Spell(110, "AllDisarm", 9, 2000, effects, null);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/FreezeCard.png");
            spellCards.add(spell);
        }//10

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,4,0,BuffType.poison,false));
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.enemy,ForceType.both,null,true);
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            Spell spell = new Spell(111, "AllPoison", 8, 1500, effects, null);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Poison_Spell_info.png");
            spellCards.add(spell);
        }//11

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.both,ForceType.both,null);
            ArrayList<Effect> effects=new ArrayList<>();

            TargetForm targetForm1=new TargetForm(0,0,1,1,SideType.enemy,ForceType.both,null,true);
            ArrayList<Buff> buffs1=new ArrayList<>();
            buffs1.add(new Buff(0,0,0,BuffType.fail_positive_buff,false));
            effects.add(new Effect(buffs1,null,targetForm1));

            TargetForm targetForm2=new TargetForm(0,0,1,1,SideType.insider,ForceType.both,null,true);
            ArrayList<Buff> buffs2=new ArrayList<>();
            buffs2.add(new Buff(0,0,0,BuffType.fail_negative_buffs,false));
            effects.add(new Effect(buffs2,null,targetForm2));

            Spell spell = new Spell(112, "Dispel", 0, 2100, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Rocket.png");
            spellCards.add(spell);
        }//12

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.insider,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,3,2,BuffType.holy,false));
            buffs.add(new Buff(0,0,6,BuffType.hurt,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(113, "HealthWithProfit", 0, 2250, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Card_icon_Heal.png");
            spellCards.add(spell);
        }//13

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.insider,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,6,BuffType.attack_power_up,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(114, "powerUp", 2, 2500, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Card_icon_Heal.png");
            spellCards.add(spell);
        }//14

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.insider,ForceType.both,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,6,BuffType.attack_power_up,true));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            Spell spell = new Spell(115, "allPower", 4, 2000, effects, null);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/RageCard.png");
            spellCards.add(spell);
        }//15

        {
            TargetForm targetForm=new TargetForm(0,-5,1,5,SideType.enemy,ForceType.both,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,6,BuffType.hurt,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            Spell spell = new Spell(116, "allAttack", 4, 1500, effects, null);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Rocket.png");
            spellCards.add(spell);
        }//16

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.enemy,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,4,BuffType.attack_power_low,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(117, "weakening", 1, 1000, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Poison_Spell_info.png");
            spellCards.add(spell);
        }//17

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.insider,ForceType.minion,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,8,BuffType.attack_power_up,false));
            buffs.add(new Buff(0,0,6,BuffType.hurt,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(118, "sacrifice", 2, 1600, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Card_icon_Heal.png");
            spellCards.add(spell);
        }//18

        {
            TargetForm targetForm=new TargetForm(-1,-1,2,2,SideType.enemy,ForceType.minion,null,false);
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.insider,ForceType.hero,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,1000,BuffType.hurt,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            Spell spell = new Spell(119, "kingsGuard", 9, 1750, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/Rocket.png");
            spellCards.add(spell);
        }//19

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.enemy,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,2,0,BuffType.stun,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Spell spell = new Spell(120, "shock", 1, 1200, effects, selectionCellPack);
            spell.getGraphicPack().setShopPhotoAddress("/resources/cards/LightningCard.png");
            spellCards.add(spell);
        }//20

        return spellCards;
    }
    public static ArrayList<Hero> defineHero(){
        ArrayList<Hero> heroes=new ArrayList<>();

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,4,BuffType.attack_power_up,true));
            HeroSpecialItemPack heroPack=new HeroSpecialItemPack(1,2,null);
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.setHeroPack(heroPack);
            specialItem.addCoolDown(new Effect(buffs,null,null));
            Hero hero=new Hero(301,"whiteDamn",0,50,8000,4, MinionTargetsType.melee,20,specialItem);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/White Daemon_logo.png");
            heroes.add(hero);
        }//1

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.enemy,ForceType.both,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,0,BuffType.stun,false));
            HeroSpecialItemPack heroPack=new HeroSpecialItemPack(5,8,null);
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.addCoolDown(new Effect(buffs,null,targetForm));
            specialItem.setHeroPack(heroPack);
            Hero hero=new Hero(302,"Simorgh",0,50,9000,4,MinionTargetsType.melee,20,specialItem);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_solfist_portrait_hex@2x.png");
            heroes.add(hero);
        }//2

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.enemy,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,0,BuffType.disarm,false));
            HeroSpecialItemPack heroPack=new HeroSpecialItemPack(0,1,selectionCellPack);
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.addCoolDown(new Effect(buffs,null,null));
            specialItem.setHeroPack(heroPack);
            Hero hero=new Hero(303,"sevenHeadDragon",0,50,8000,4,MinionTargetsType.melee,20,specialItem);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/SevenHead Dragon_logo.png");
            heroes.add(hero);
        }//3

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.enemy,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,1,BuffType.stun,true));
            HeroSpecialItemPack heroPack=new HeroSpecialItemPack(1,2,selectionCellPack);
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.addCoolDown(new Effect(buffs,null,null));
            specialItem.setHeroPack(heroPack);
            Hero hero=new Hero(304,"Rakhsh",0,50,8000,4,MinionTargetsType.melee,20,specialItem);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/Rakhsh_logo.png");
            heroes.add(hero);
        }//4

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,3,0,BuffType.poison,false));
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.addAttack(new Effect(buffs,null,null));
            Hero hero=new Hero(305,"Zahak",0,50,10000,2,MinionTargetsType.melee,20,specialItem);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/Zahak_logo.png");
            heroes.add(hero);
        }//5

        {
            ArrayList<HouseEffect> houseEffects=new ArrayList<>();
            houseEffects.add(new HouseEffect(HouseEffectType.holly,1,3,0));
            HeroSpecialItemPack heroPack=new HeroSpecialItemPack(1,3,null);
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.addCoolDown(new Effect(null,houseEffects,null));
            specialItem.setHeroPack(heroPack);
            Hero hero=new Hero(306,"Kave",0,50,8000,4,MinionTargetsType.melee,20,specialItem);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/Kaveh_logo.png");
            heroes.add(hero);
        }//6

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.enemy,ForceType.hero,null);
            TargetForm targetForm=new TargetForm(-9,0,9,1,SideType.both,ForceType.both,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,4,BuffType.hurt,true));
            HeroSpecialItemPack heroPack=new HeroSpecialItemPack(2,2,selectionCellPack);
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.addCoolDown(new Effect(buffs,null,targetForm));
            specialItem.setHeroPack(heroPack);
            Hero hero=new Hero(307,"Arash",0,30,10000,2,MinionTargetsType.ranged,6,specialItem);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/Arash_logo.png");
            heroes.add(hero);
        }//7

        {
            SelectionCellPack selectionCellPack=new SelectionCellPack(SideType.enemy,ForceType.both,null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,0,BuffType.fail_positive_buff,true));
            HeroSpecialItemPack heroPack=new HeroSpecialItemPack(1,2,selectionCellPack);
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.addCoolDown(new Effect(buffs,null,null));
            specialItem.setHeroPack(heroPack);
            Hero hero=new Hero(308,"Afsane",0,40,11000,3,MinionTargetsType.ranged,3,specialItem);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_andromeda_portrait_hex@2x.png");
            heroes.add(hero);
        }//8

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,3,BuffType.holy,false));
            HeroSpecialItemPack heroPack=new HeroSpecialItemPack(0,0,null);
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.addCoolDown(new Effect(buffs,null,null));
            specialItem.setHeroPack(heroPack);
            Hero hero=new Hero(309,"Esfandiar",0,35,12000,3,MinionTargetsType.hybird,3,specialItem);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_malyk_portrait_hex@2x.png");
            heroes.add(hero);
        }//9

        {
            Hero hero=new Hero(310,"Rostam",0,55,10000,7,MinionTargetsType.hybird,4,null);
            hero.getGraphicPack().setShopPhotoAddress("/resources/cards/Rostam_logo.png");
            heroes.add(hero);
        }//10

        return heroes;
    }
    public static ArrayList<Minion> defineMinion(){
        ArrayList<Minion> minions=new ArrayList<>();

        {
            Minion minion=new Minion(201,"FarsArcher",2,6,7,300,CardType.minion,
                    MinionTargetsType.ranged,4,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_andromeda_portrait_hex@2x.png");
            minions.add(minion);
        }//1

        {
            SpecialItem specialItem=new SpecialItem(null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,0,BuffType.stun,false));
            Effect effect=new Effect(buffs,null,null);
            specialItem.addAttack(effect);
            Minion minion=new Minion(202,"FarsSwordsman",2,6,20,400,CardType.minion, MinionTargetsType.melee,4,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_antiswarm_portrait_hex@2x.png");
            minions.add(minion);
        }//2

        {
            Minion minion=new Minion(203,"FarsSpear",1,5,3,500,CardType.minion,
                    MinionTargetsType.hybird,6,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_archonis_portrait_hex@2x.png");
            minions.add(minion);
        }//3

        {
            Minion minion=new Minion(204,"FarsHorseman",4,10,20,200,CardType.minion,
                    MinionTargetsType.melee,6,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_boreal_juggernaut_portrait_image_hex@2x.png");
            minions.add(minion);
        }//4

        {
            SpecialOption option=new SpecialOption();
            option.setMore_ap_by_number_of_least_attacks(true);
            SpecialItem specialItem=new SpecialItem(option);
            Minion minion=new Minion(205,"FarsGladiator",9,24,20,400,CardType.minion,
                    MinionTargetsType.melee,6,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_calibero_portrait_image_hex@2x.png");
            minions.add(minion);
        }//5

        {

            SpecialItem specialItem=new SpecialItem(null);
            specialItem.setCombo(true);
            Minion minion=new Minion(206,"FarsSepahsalar",7,12,20,800,CardType.minion,
                    MinionTargetsType.melee,4,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_chaos_knight_portrait_hex@2x.png");
            minions.add(minion);
        }//6

        {
            Minion minion=new Minion(207,"ToraniArcher",1,3,20,500,CardType.minion,
                    MinionTargetsType.ranged,4,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_christmas_portrait_hex@2x.png");
            minions.add(minion);
        }//7

        {
            Minion minion=new Minion(208,"ToraniHooked",1,4,7,600,CardType.minion,
                    MinionTargetsType.ranged,2,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_cindera_portrait_hex@2x.png");
            minions.add(minion);
        }//8

        {
            Minion minion=new Minion(209,"ToraniSpear",1,4,3,600,CardType.minion,
                    MinionTargetsType.hybird,4,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_crystal_portrait_hex@2x.png");
            minions.add(minion);
        }//9

        {
            SpecialItem specialItem=new SpecialItem(null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,0,BuffType.disarm,false));
            buffs.add(new Buff(0,4,0,BuffType.poison,false));
            Effect effect=new Effect(buffs,null,null);
            specialItem.addAttack(effect);
            Minion minion=new Minion(210,"ToraniSpy",4,6,20,700,CardType.minion,
                    MinionTargetsType.melee,6,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_crystal_portrait_hex@2x.png");
            minions.add(minion);
        }//10

        {
            Minion minion=new Minion(211,"ToraniSwampy",2,3,20,450,CardType.minion,
                    MinionTargetsType.melee,10,null);
            minions.add(minion);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_boreal_juggernaut_portrait_image_hex@2x.png");
        }//11

        {
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.setCombo(true);
            Minion minion=new Minion(212,"ToraniPrince",6,6,20,800,CardType.minion,
                    MinionTargetsType.melee,10,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_chaos_knight_portrait_hex@2x.png");
            minions.add(minion);
        }//12

        {
            Minion minion=new Minion(213,"blackDamn",9,14,7,300,CardType.minion,
                    MinionTargetsType.hybird,10,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_christmas_portrait_hex@2x.png");
            minions.add(minion);
        }//13

        {
            Minion minion=new Minion(214,"slumdogGiant",9,12,7,300,CardType.minion,
                    MinionTargetsType.ranged,12,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_shinkage_zendo_portrait_image_hex@2x.png");
            minions.add(minion);
        }//14

        {
            SpecialItem specialItem=new SpecialItem(null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,10,BuffType.health_rise,false));
            Effect effect=new Effect(buffs,null,null);
            specialItem.addPassive(effect);
            Minion minion=new Minion(215,"eagle",2,0,3,200,CardType.minion,
                    MinionTargetsType.ranged,2,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_shinkage_zendo_portrait_image_hex@2x.png");
            minions.add(minion);
        }//15

        {
            Minion minion=new Minion(216,"boutRiderDamn",6,16,20,300,CardType.minion,
                    MinionTargetsType.melee,8,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_umbra_portrait_hex@2x.png");
            minions.add(minion);
        }//16

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(-1,-1,2,2,SideType.both,ForceType.both,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,2,BuffType.hurt,false));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addDeath(effect);
            Minion minion=new Minion(217,"singleEyeGiant",7,12,3,500,CardType.minion,
                    MinionTargetsType.hybird,11,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_umbra_portrait_hex@2x.png");
            minions.add(minion);
        }//17

        {
            SpecialItem specialItem=new SpecialItem(null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,3,0,BuffType.poison,false));
            Effect effect=new Effect(buffs,null,null);
            specialItem.addAttack(effect);
            Minion minion=new Minion(218,"poisonousSnake",4,5,4,300,CardType.minion,
                    MinionTargetsType.ranged,6,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wujin_portrait_hex@2x.png");
            minions.add(minion);
        }//18

        {
            Minion minion=new Minion(219,"lighthouseDragon",5,9,4,250,CardType.minion,
                    MinionTargetsType.ranged,5,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wujin_portrait_hex@2x.png");
            minions.add(minion);
        }//19

        {
            SpecialOption specialOption=new SpecialOption();
            specialOption.setPositiveBuffs_of_Enemy_not_effect(true);
            SpecialItem specialItem=new SpecialItem(specialOption);
            Minion minion=new Minion(220,"wildLion",2,1,4,600,CardType.minion,
                    MinionTargetsType.ranged,8,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wraith_portrait_hex@2x.png");
            minions.add(minion);
        }//20

        {
            SpecialItem specialItem=new SpecialItem(null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,-1,BuffType.holy,true));
            TargetForm targetForm1=new TargetForm(-2,0,0,1,SideType.both,ForceType.minion,null,true);
            TargetForm targetForm2=new TargetForm(0,-2,1,0,SideType.both,ForceType.minion,null,true);
            TargetForm targetForm3=new TargetForm(0,1,1,3,SideType.both,ForceType.minion,null,true);
            TargetForm targetForm4=new TargetForm(1,0,3,1,SideType.both,ForceType.minion,null,true);
            specialItem.addSpawn(new Effect(buffs,null,targetForm1));
            specialItem.addSpawn(new Effect(buffs,null,targetForm2));
            specialItem.addSpawn(new Effect(buffs,null,targetForm3));
            specialItem.addSpawn(new Effect(buffs,null,targetForm4));
            Minion minion=new Minion(221,"hugeSnake",8,14,5,500,CardType.minion
            ,MinionTargetsType.ranged,7,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wraith_portrait_hex@2x.png");
            minions.add(minion);
        }//21

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(0,0,1,1,SideType.enemy,ForceType.minion,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,6,BuffType.hurt,false));
            buffs.add(new Buff(1,1,4,BuffType.hurtTime,false));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addAttack(effect);
            Minion minion=new Minion(222,"whiteWolf",5,8,20,400,CardType.minion,
                    MinionTargetsType.melee,2,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wraith_portrait_hex@2x.png");
            minions.add(minion);
        }//22

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(0,0,1,1,SideType.enemy,ForceType.minion,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(1,1,8,BuffType.hurtTime,false));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addAttack(effect);
            Minion minion=new Minion(223,"leopard",4,6,20,400,CardType.minion,
                    MinionTargetsType.melee,2,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_unhallowed_portrait_hex@2x.png");
            minions.add(minion);
        }//23

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(0,0,1,1,SideType.enemy,ForceType.minion,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(1,1,6,BuffType.hurtTime,false));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addAttack(effect);
            Minion minion=new Minion(224,"wolf",3,6,20,400,CardType.minion,
                    MinionTargetsType.melee,1,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_umbra_portrait_hex@2x.png");
            minions.add(minion);
        }//24

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(-1,-1,2,2,SideType.insider,ForceType.both,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,2,BuffType.attack_power_up,false));
            buffs.add(new Buff(0,0,1,BuffType.hurt,false));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addPassive(effect);
            Minion minion=new Minion(225,"magician",4,5,3,550,CardType.minion,
                    MinionTargetsType.ranged,4,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_umbra_portrait_hex@2x.png");
            minions.add(minion);
        }//25

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(-1,-1,2,2,SideType.insider,ForceType.both,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,2,BuffType.attack_power_up,true));
            buffs.add(new Buff(0,1000,4,BuffType.holy,true));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addPassive(effect);
            Minion minion=new Minion(226,"enormousMagician",6,6,5,550,CardType.minion,
                    MinionTargetsType.ranged,6,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/general_portrait_image_hex_rook.png");
            minions.add(minion);
        }//26

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.insider,ForceType.minion,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,1,BuffType.attack_power_up,true));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addSpawn(effect);
            Minion minion=new Minion(227,"elf",5,10,4,500,CardType.minion,
                    MinionTargetsType.ranged,4,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/general_portrait_image_hex_rook.png");
            minions.add(minion);
        }//27

        {
            SpecialOption option=new SpecialOption();
            option.setNotDisarm(true);
            SpecialItem specialItem=new SpecialItem(option);
            Minion minion=new Minion(228,"wildHog",6,10,20,500,CardType.minion,
                    MinionTargetsType.melee,14,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wraith_portrait_hex@2x.png");
            minions.add(minion);
        }//28

        {
            SpecialOption option=new SpecialOption();
            option.setNotPoison(true);
            SpecialItem specialItem=new SpecialItem(option);
            Minion minion=new Minion(229,"Piran",8,20,20,400,CardType.minion,
                    MinionTargetsType.melee,12,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wraith_portrait_hex@2x.png");
            minions.add(minion);
        }//29

        {
            SpecialOption option=new SpecialOption();
            option.setNot_Negative_Buff(true);
            SpecialItem specialItem=new SpecialItem(option);
            Minion minion=new Minion(230,"Giv",4,5,5,450,CardType.minion,
                    MinionTargetsType.ranged,7,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wraith_portrait_hex@2x.png");
            minions.add(minion);
        }//30

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.enemy,ForceType.minion,null,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,16,BuffType.hurt,false));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addSpawn(effect);
            Minion minion=new Minion(231,"Bahman",8,16,20,450,CardType.minion,
                    MinionTargetsType.melee,9,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_vampire_portrait_hex@2x.png");
            minions.add(minion);
        }//31

        {
            SpecialOption option=new SpecialOption();
            option.setNot_attack_by_fewer_ap(true);
            SpecialItem specialItem=new SpecialItem(option);
            Minion minion=new Minion(232,"Ashkboos",7,14,20,400,CardType.minion,
                    MinionTargetsType.melee,8,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_vampire_portrait_hex@2x.png");
            minions.add(minion);
        }//32

        {
            Minion minion=new Minion(233,"Eraj",4,6,3,500,CardType.minion,
                    MinionTargetsType.ranged,20,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_vampire_portrait_hex@2x.png");
            minions.add(minion);
        }//33

        {
            Minion minion=new Minion(234,"hugeGiant",9,30,20,600,CardType.minion,
                    MinionTargetsType.hybird,8,null);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wraith_portrait_hex@2x.png");
            minions.add(minion);

        }//34

        {
            SpecialItem specialItem=new SpecialItem(null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,0,BuffType.fail_positive_buff,false));
            Effect effect=new Effect(buffs,null,null);
            specialItem.addAttack(effect);
            Minion minion=new Minion(235,"DoubleHeadGiant",4,10,20,550,CardType.minion,
                    MinionTargetsType.melee,4,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wraith_portrait_hex@2x.png");
            minions.add(minion);
        }//35

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(-1,-1,2,2,SideType.enemy,ForceType.minion,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,0,BuffType.stun,false));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addSpawn(effect);
            Minion minion=new Minion(236,"coldMather",3,3,5,550,CardType.minion,
                    MinionTargetsType.ranged,4,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wraith_portrait_hex@2x.png");
            minions.add(minion);

        }//36

        {
            SpecialItem specialItem=new SpecialItem(null);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,12,BuffType.holy,false));
            Effect effect=new Effect(buffs,null,null);
            specialItem.addPassive(effect);
            Minion minion=new Minion(237,"steelArmor",3,1,20,650,CardType.minion,
                    MinionTargetsType.melee,1,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wujin_portrait_hex@2x.png");
            minions.add(minion);
        }//37

        {
            SpecialItem specialItem=new SpecialItem(null);
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.enemy,ForceType.hero,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,6,BuffType.hurt,false));
            Effect effect=new Effect(buffs,null,targetForm);
            specialItem.addDeath(effect);
            Minion minion=new Minion(238,"Siavash",4,8,20,350,CardType.minion,
                    MinionTargetsType.melee,5,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wujin_portrait_hex@2x.png");
            minions.add(minion);
        }//38

        {
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.setCombo(true);
            Minion minion=new Minion(239,"giant king",5,10,20,600,CardType.minion,
                    MinionTargetsType.melee,4,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wujin_portrait_hex@2x.png");
            minions.add(minion);
        }//39

        {
            SpecialItem specialItem=new SpecialItem(null);
            specialItem.setCombo(true);
            Minion minion=new Minion(240,"ArjangDamn",3,6,20,600,CardType.minion,
                    MinionTargetsType.melee,6,specialItem);
            minion.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_wujin_portrait_hex@2x.png");
            minions.add(minion);
        }//40
        return minions;
    }
    public static ArrayList<Item> defineItem(){
        ArrayList<Item> items=new ArrayList<>();

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,3,1,BuffType.mana,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Item item=new Item(401, ItemType.usable,300,"CrownOfWisdom",effects
                    ,true,false,null,null);
            items.add(item);

        }//1

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,1,BuffType.holy,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            Item item=new Item(402,ItemType.usable,4000,"namoosSepar",effects
                    ,true,false,null,null);

            items.add(item);
        }//2

        {
            MinionType minionType=new MinionType(false,true,true);
            TargetForm targetForm=new TargetForm(0,0,1,1,SideType.enemy
                    ,ForceType.both,minionType,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,0,BuffType.disarm,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            Item item=new Item(403,ItemType.usable,30000,"kamanDamol",effects
                    ,false,false, ItemActivateTime.onAttack,null);
            items.add(item);
        }//3

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.both,ForceType.both,null,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,6,BuffType.health_rise,true));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            Item item=new Item(412,ItemType.collectable,0,"nooshdaroo",effects
                    ,true,false,null,null);
            items.add(item);
        }//4

        {
            MinionType minionType=new MinionType(false,true,true);
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.both,ForceType.both,minionType,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,2,BuffType.attack_power_up,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(413,ItemType.collectable,0,"TirDoShakh",effects
            ,true,false,null,null));
        }//5

        {
            MinionType minionType=new MinionType(false,true,true);
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.enemy,ForceType.hero,minionType,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,2,BuffType.attack_power_low,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(414,ItemType.usable,3500,"parSimorgh",effects
                    ,true,false,null,null));
        }//6

        {
            TargetForm targetForm1=new TargetForm(-9,-5,9,5,SideType.both,ForceType.minion,null,false);
            ArrayList<Buff> buffs1=new ArrayList<>();
            buffs1.add(new Buff(0,1000,3,BuffType.attack_power_up,false));

            ArrayList<Buff> buffs2=new ArrayList<>();
            buffs2.add(new Buff(0,1000,3,BuffType.health_rise,false));

            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs1,null,targetForm1));
            effects.add(new Effect(buffs2,null,null));
            items.add(new Item(415,ItemType.collectable,0,"exir",effects
                    ,true,false,null,null));
        }//7

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,3,BuffType.mana,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            items.add(new Item(416,ItemType.collectable,0,"manaPotion",effects
                    ,true,false,null,null));
        }//8

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.insider,ForceType.both,null,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,2,10,BuffType.holy,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(417,ItemType.collectable,0,"growthPotion",effects
                    ,true,false,null,null));
        }//9

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.both,ForceType.both,null,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,8,BuffType.hurt,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            TargetForm randomForce=new TargetForm(-9,-5,9,5,SideType.both,ForceType.both,null,false);
            items.add(new Item(418,ItemType.collectable,0,"nefrinMarg",effects
                    ,false,false,ItemActivateTime.onDeath,randomForce));
        }//10

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.both,ForceType.both,null,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,2,BuffType.attack_power_up,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(419,ItemType.collectable,0,"RandomDamage",effects
                    ,true,false,null,null));
        }//11

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.enemy,ForceType.both,null,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,2,BuffType.attack_power_low,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(405,ItemType.usable,5000,"TerrorHood",effects
                    ,false,false,ItemActivateTime.onAttack,null));
        }//12

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.both,ForceType.both,null,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,6,BuffType.attack_power_up,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(420,ItemType.collectable,0,"BladesOFAgility",effects
                    ,true,false,null,null));
        }//13

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.insider,ForceType.hero,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,1,BuffType.mana,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            items.add(new Item(406,ItemType.usable,9000,"kingWisdom",effects
                    ,true,false,null,null));
        }//14

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.enemy,ForceType.hero,null,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,0,1,BuffType.hurt,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(407,ItemType.usable,15000,"AssassinationDagger",effects
                    ,false,true,ItemActivateTime.onSpawn,null));
        }//15

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.enemy,ForceType.both,null,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,0,BuffType.poison,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(408,ItemType.usable,7000,"poisonousDagger",effects
                    ,false,true,ItemActivateTime.onAttack,null));
        }//16

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1,0,BuffType.disarm,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            items.add(new Item(409,ItemType.usable,15000,"shockHammer",effects
                    ,false,false,ItemActivateTime.onAttack,null));
        }//17

        {
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.insider,ForceType.both,null,false);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,1,BuffType.attack_power_up,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(410,ItemType.usable,25000,"soulEater",effects
                    ,false,true,ItemActivateTime.onDeath,null));
        }//18

        {
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,2,1,BuffType.holy,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,null));
            items.add(new Item(411,ItemType.usable,20000,"ghoslTamid",effects
                    ,false,true,ItemActivateTime.onSpawn,null));
        }//19

        {
            MinionType minionType=new MinionType(true,false,false);
            TargetForm targetForm=new TargetForm(-9,-5,9,5,SideType.both,ForceType.both,minionType,true);
            ArrayList<Buff> buffs=new ArrayList<>();
            buffs.add(new Buff(0,1000,5,BuffType.attack_power_up,false));
            ArrayList<Effect> effects=new ArrayList<>();
            effects.add(new Effect(buffs,null,targetForm));
            items.add(new Item(420,ItemType.collectable,0,"shamshirChini",effects
                    ,true,false,null,null));
        }//20

        return items;
    }
    public static ArrayList<Card> defineCard(){
        ArrayList<Card> cards=new ArrayList<>();
        cards.addAll(defineHero());
        cards.addAll(defineMinion());
        cards.addAll(defineSpell());
        return cards;
    }
    public static ArrayList<Item> all_item_by_type(ItemType type){
        ArrayList<Item> items=new ArrayList<>();
        ArrayList<Item> allItems=defineItem();
        for (Item item:allItems){
            if (item.getItemType()==type)items.add(item);
        }
        return items;
    }
    public static Deck maker(int[] cardsCodes, int itemCode, int heroCode){
        ArrayList<Card> allCards = defineCard();
        ArrayList<Item> allItem=defineItem();
        ArrayList<Card> myCards=new ArrayList<>();
        ArrayList<Hero> allHero=defineHero();
        Item myItem=null;
        if (itemCode!=0){
            for (Item item:allItem){
                if (item.getCode()==itemCode){
                    myItem=item;
                    break;
                }
            }
        }
        for (Integer code:cardsCodes){
            for (Card card:allCards){
                if (card.getCode()==code){
                    myCards.add(card);
                    break;
                }
            }
        }
        for (Hero hero:allHero){
            if (hero.getCode()==heroCode)myCards.add(hero);
        }

        return new Deck("pcDeck",myCards,myItem);
    }
}

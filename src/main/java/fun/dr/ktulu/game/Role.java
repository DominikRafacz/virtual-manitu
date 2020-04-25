package fun.dr.ktulu.game;

import fun.dr.ktulu.game.exception.RoleNotMatchedException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static fun.dr.ktulu.game.Faction.*;

public enum Role {
    DZIWKA("Dziwka", MIASTO),
    UWODZICIEL("Uwodziciel", MIASTO),
    SZANTAZYSTA("Szantażysta", BANDYCI),
    SZERYF("Szeryf", MIASTO),
    PASTOR("Pastor", MIASTO),
    POBORCA_PODATKOW("Poborca Podatków", MIASTO),
    OCHRONIARZ("Ochroniarz", MIASTO),
    HAZARDZISTA("Hazardzista", MIASTO),
    OPOJ("Opój", MIASTO),
    PIJANY_SEDZIA("Pijany Sędzia", MIASTO),
    KAT("Kat", MIASTO),
    MSCICIEL("Mściciel", BANDYCI),
    ZLODZIEJ("Złodziej", BANDYCI),
    SZULER("Szuler", BANDYCI),
    SZAMAN("Szaman", INDIANIE),
    PLONACY_SZAL("Płonący Szał", INDIANIE),
    SZAMANKA("Szamanka", INDIANIE),
    WOJOWNIK("Wojownik", INDIANIE),
    CICHA_STOPA("Cicha Stopa", INDIANIE),
    LORNECIE_OKO("Lornecie Oko", INDIANIE),
    DETEKTOR("Detektor", UFOLE),
    POZERACZ_UMYSLOW("Pożeracz Umysłów", UFOLE),
    ZIELONA_MACKA("Zielona Macka", UFOLE),
    PURPUROWA_PRZYSSAWKA("Purpurowa Przyssawka", UFOLE),
    DOBRY_REWOLWEROWIEC("Dobry Rewolwerowiec", MIASTO),
    LEKARZ("Lekarz", MIASTO),
    SEDZIA("Sędzia", MIASTO),
    HERSZT("Herszt", BANDYCI),
    ZLY_REWOLWEROWIEC("Zły Rewolwerowiec", BANDYCI),
    WODZ("Wódz", INDIANIE),
    WIELKI_UFOL("Wielki Ufol", UFOLE);

    @Getter
    private final String name;
    @Getter
    private final Faction faction;
    private static final EnumSet<Role> ALL_ROLES = EnumSet.allOf(Role.class);

    Role(String name, Faction faction) {
        this.name = name;
        this.faction = faction;
    }

    public static @NotNull Role matchRole(String roleName) throws RoleNotMatchedException {
        Optional<Role> matchedRole = ALL_ROLES.stream()
                .filter(role -> role.getName().equals(roleName))
                .findFirst();
        if (matchedRole.isEmpty()) throw new RoleNotMatchedException();
        return matchedRole.get();
    }
}

package fun.dr.ktulu.game;

import fun.dr.ktulu.game.exception.RoleNotMatchedException;
import fun.dr.ktulu.messaging.command.exception.ValidationException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fun.dr.ktulu.game.Faction.*;

public enum Role {
    // MIASTO
    SZERYF("Szeryf", MIASTO, numPlayers -> true),
    DZIWKA("Dziwka", MIASTO, numPlayers -> true),
    PASTOR("Pastor", MIASTO, numPlayers -> true),
    DOBRY_REWOLWEROWIEC("Dobry Rewolwerowiec", MIASTO, numPlayers -> true),
    PIJANY_SEDZIA("Pijany Sędzia", MIASTO, numPlayers -> numPlayers <= 12),
    SEDZIA("Sędzia", MIASTO, numPlayers -> numPlayers >= 13),
    OPOJ("Opój", MIASTO, numPlayers -> numPlayers >= 13),
    BURMISTRZ("Burmistrz", MIASTO, numPlayers -> numPlayers >= 15 && numPlayers != 18),
    OCHRONIARZ("Ochroniarz", MIASTO, numPlayers -> numPlayers >= 20),
    POBORCA_PODATKOW("Poborca Podatków", MIASTO, numPlayers -> numPlayers >= 22),
    LEKARZ("Lekarz", MIASTO, numPlayers -> numPlayers >= 24),
    UWODZICIEL("Uwodziciel", MIASTO, numPlayers -> numPlayers >= 26),
    HAZARDZISTA("Hazardzista", MIASTO, numPlayers -> numPlayers >= 28),
    KAT("Kat", MIASTO, numPlayers -> numPlayers >= 30),
    // BANDYCI
    ZLY_REWOLWEROWIEC("Zły Rewolwerowiec", BANDYCI, numPlayers -> true),
    SZANTAZYSTA("Szantażysta", BANDYCI, numPlayers -> true),
    MSCICIEL("Mściciel", BANDYCI, numPlayers -> true),
    ZLODZIEJ("Złodziej", BANDYCI, numPlayers -> true),
    // should Herszt and Szuler be swapped?
    HERSZT("Herszt", BANDYCI, numPlayers -> (numPlayers >= 16 && numPlayers < 18) || numPlayers >= 25),
    SZULER("Szuler", BANDYCI, numPlayers -> numPlayers >= 29),
    // INDIANIE
    SZAMAN("Szaman", INDIANIE, numPlayers -> true),
    SZAMANKA("Szamanka", INDIANIE, numPlayers -> true),
    WOJOWNIK("Wojownik", INDIANIE, numPlayers -> true),
    LORNECIE_OKO("Lornecie Oko", INDIANIE, numPlayers -> numPlayers >= 14),
    SAMOTNY_KOJOT("Samotny Kojot", INDIANIE, numPlayers -> numPlayers >= 17),
    CICHA_STOPA("Cicha Stopa", INDIANIE, numPlayers -> numPlayers >= 21),
    PLONACY_SZAL("Płonący Szał", INDIANIE, numPlayers -> numPlayers >= 27),
    WODZ("Wódz", INDIANIE, numPlayers -> false),
    // UFOLE
    POZERACZ_UMYSLOW("Pożeracz Umysłów", UFOLE, numPlayers -> numPlayers >= 18),
    ZIELONA_MACKA("Zielona Macka", UFOLE, numPlayers -> numPlayers >= 18),
    DETEKTOR("Detektor", UFOLE, numPlayers -> numPlayers >= 18),
    WIELKI_UFOL("Wielki Ufol", UFOLE, numPlayers -> numPlayers >= 23),
    PURPUROWA_PRZYSSAWKA("Purpurowa Przyssawka", UFOLE, numPlayers -> false);

    @Getter
    private final String name;
    @Getter
    private final Faction faction;
    private final Function<Integer, Boolean> include;
    private static final EnumSet<Role> ALL_ROLES = EnumSet.allOf(Role.class);

    Role(String name, Faction faction, Function<Integer, Boolean> include) {
        this.name = name;
        this.faction = faction;
        this.include = include;
    }

    public static @NotNull Role matchRole(String roleName) throws RoleNotMatchedException {
        Optional<Role> matchedRole = ALL_ROLES.stream()
                .filter(role -> role.getName().equals(roleName))
                .findFirst();
        if (matchedRole.isEmpty()) throw new RoleNotMatchedException();
        return matchedRole.get();
    }

    public static @NotNull Set<Role> getDefaultRoles(Integer numPlayers) throws ValidationException {
        if (numPlayers < 12)
            throw new ValidationException(
                    "Wybacz, nie znam składu dla mniej niż 12 osób. Ale, serio, nie lepiej zagrać w Mafię?");
        if (numPlayers > 30)
            throw new ValidationException(
                    "Uch, to mnóstwo osób. Mogę zaproponować domyślną trzydziestkę, a resztę dodaj ręcznie.");
        return ALL_ROLES.stream()
                .filter(role -> role.include.apply(numPlayers))
                .collect(Collectors.toSet());
    }
}

package fun.dr.ktulu.game;

import fun.dr.ktulu.bot.AppManager;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class Ktulowiec {
    @Getter
    protected final String userID;

    public Ktulowiec(String userID) {
        this.userID = userID;
    }

    public Member getAsMember() {
        return Objects.requireNonNull(AppManager.getInstance()
                .getJda()
                .getGuildById(Game.getInstance().getGuildID()))
                .getMemberById(userID);
    }

    public String getTempName() {
        Member member = getAsMember();
        return member.getNickname() == null ? member.getUser().getName() : member.getNickname();
    }
}

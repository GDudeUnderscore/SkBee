package com.shanebeestudios.skbee.elements.bound.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.shanebeestudios.skbee.api.util.WorldUtils;
import com.shanebeestudios.skbee.elements.bound.objects.Bound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

@Name("Bound - Resize")
@Description({"Resize a current bound. Full will stretch it to the lowest/highest points of the world.",
        "\nNOTE: World of a bound cannot be changed."})
@Examples("resize bound with id \"test\" between {_l1} and {_l1}")
@Since("INSERT VERSION")
public class EffBoundResize extends Effect {

    static {
        Skript.registerEffect(EffBoundResize.class,
                "resize [:full] bound %bound% (within|between) %location% and %location%");
    }

    private Expression<Bound> bound;
    private Expression<Location> loc1, loc2;
    private boolean full;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.bound = (Expression<Bound>) exprs[0];
        this.loc1 = (Expression<Location>) exprs[1];
        this.loc2 = (Expression<Location>) exprs[2];
        this.full = parseResult.hasTag("full");
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        Bound bound = this.bound.getSingle(event);
        Location loc1 = this.loc1.getSingle(event);
        Location loc2 = this.loc2.getSingle(event);
        if (bound == null || loc1 == null || loc2 == null) return;

        World world = bound.getWorld();
        if (loc1.getWorld() != world || loc2.getWorld() != world) return;

        if (full) {
            loc1 = loc1.clone();
            loc2 = loc2.clone();

            int max = world.getMaxHeight() - 1;
            int min = WorldUtils.getMinHeight(world);
            loc1.setY(min);
            loc2.setY(max);
        }
        bound.resize(loc1, loc2);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String full = this.full ? "full " : "";
        return "resize " + full + "bound " + this.bound.toString(e, d) + " within " +
                this.loc1.toString(e, d) + " and " + this.loc2.toString(e, d);
    }

}
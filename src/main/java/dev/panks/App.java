package dev.panks;

import dev.panks.ActorsAwardsAggregator.Actor;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 *
 */
public class App {

  public static final Comparator<Actor> ACTOR_COMPARATOR = Comparator.comparing((Actor actor) -> actor.numberOfAwards)
                                                       .reversed()
                                                       .thenComparing(
                                                         Comparator.comparing((Actor actor) -> actor.yearOfBirth).reversed())
                                                       .thenComparing((Actor actor) -> actor.name);
  public static final Predicate<Actor> ACTOR_PREDICATE_FILTER = actor -> actor.numberOfAwards > 1;

  public static void main( String[] args ) throws Exception {

    new ActorsAwardsAggregator()
            .aggregateAwardDataFromCSVFiles(ACTOR_PREDICATE_FILTER, ACTOR_COMPARATOR,
                          Path.of(ClassLoader.getSystemResource("oscar_age_female.csv").toURI()),
                          Path.of(ClassLoader.getSystemResource("oscar_age_malecsv.csv").toURI())
                        )
          .forEach(actor -> System.out.printf("Actor %s born in %s with number of awards %s\n",
            actor.name, actor.yearOfBirth, actor.numberOfAwards));
    }

}

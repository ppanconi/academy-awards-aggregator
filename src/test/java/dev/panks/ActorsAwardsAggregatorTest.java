package dev.panks;

import static dev.panks.App.ACTOR_COMPARATOR;
import static dev.panks.App.ACTOR_PREDICATE_FILTER;

import dev.panks.ActorsAwardsAggregator.Actor;
import dev.panks.ActorsAwardsAggregator.AnnualAwardDataInfo;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ActorsAwardsAggregatorTest {

  ActorsAwardsAggregator aggregator;

  @BeforeEach
  void setUp() {
    aggregator = new ActorsAwardsAggregator();
  }

  @Test
  void aggregateAwardData() {

    List<Actor> actors = aggregator.aggregateAwardData(
      List.of(
        new AnnualAwardDataInfo(1, 1990, 20, "A", "abc"),
        new AnnualAwardDataInfo(2, 1991, 40, "B", "tyw"),
        new AnnualAwardDataInfo(3, 1992, 22, "A", "ghw"),
        new AnnualAwardDataInfo(4, 1993, 42, "B", "iwa"),
        new AnnualAwardDataInfo(5, 1994, 29, "C", "fsa"),
        new AnnualAwardDataInfo(6, 1995, 44, "B", "yet"),
        new AnnualAwardDataInfo(7, 1996, 31, "C", "oph"),
        new AnnualAwardDataInfo(8, 1997, 17, "D", "oph")),
      ACTOR_PREDICATE_FILTER,  ACTOR_COMPARATOR).collect(Collectors.toList());

    assertThat(actors).hasSize(3);
    assertThat(actors.get(0).name).isEqualTo("B");
    assertThat(actors.get(1).name).isEqualTo("A");
    assertThat(actors.get(2).name).isEqualTo("C");
    assertThat(actors.get(0).numberOfAwards).isEqualTo(3);
    assertThat(actors.get(1).numberOfAwards).isEqualTo(2);
    assertThat(actors.get(2).numberOfAwards).isEqualTo(2);
    assertThat(actors.get(0).yearOfBirth).isEqualTo(1951);
    assertThat(actors.get(1).yearOfBirth).isEqualTo(1970);
    assertThat(actors.get(2).yearOfBirth).isEqualTo(1965);

  }

  @Test
  void aggregateAwardDataFromCSVFiles() {
  }
}
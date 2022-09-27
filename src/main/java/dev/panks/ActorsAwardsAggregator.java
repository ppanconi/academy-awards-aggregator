package dev.panks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActorsAwardsAggregator {

  public static class AnnualAwardDataInfo {
    public int index;
    public int year;
    public int age;
    public String name;
    public String movie;
    private int actorYearOfBirth;

    public AnnualAwardDataInfo(int index, int year, int age, String name, String movie) {
      this.index = index;
      this.year = year;
      this.age = age;
      this.name = name;
      this.movie = movie;
    }

    private AnnualAwardDataInfo(int index, int year, int age, String name, String movie, int actorYearOfBirth) {
      this.index = index;
      this.year = year;
      this.age = age;
      this.name = name;
      this.movie = movie;
      this.actorYearOfBirth = actorYearOfBirth;
    }

    private AnnualAwardDataInfo withYearOfBirth(int yearOfBirth) {
      return new AnnualAwardDataInfo(this.index, this.year, this.age, this.name, this.movie, yearOfBirth);
    }

    private static AnnualAwardDataInfo fromArray(String[] array) {
      return new AnnualAwardDataInfo(Integer.parseInt(array[0]),
        Integer.parseInt(array[1]),
        Integer.parseInt(array[2]),
        array[3],
        array[4]);
    }
  }

  static class Actor {
    public String name;
    public int yearOfBirth;
    public long numberOfAwards;

    public Actor(String name, int yearOfBirth) {
      this.name = name;
      this.yearOfBirth = yearOfBirth;
    }

    public Actor(String name, int yearOfBirth, long numberOfAwards) {
      this.name = name;
      this.yearOfBirth = yearOfBirth;
      this.numberOfAwards = numberOfAwards;
    }

    public Actor withNumberOfAwards(long numberOfAwards) {
      return new Actor(this.name, this.yearOfBirth, numberOfAwards);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Actor actor = (Actor) o;
      return name.equals(actor.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }

  public Stream<Actor> aggregateAwardData(Collection<AnnualAwardDataInfo> data,
                                            Predicate<Actor> filter,
                                            Comparator<Actor> comparator) {

    return aggregateAwardData(data.stream(), filter, comparator);
  }

  private Stream<Actor> aggregateAwardData(Stream<AnnualAwardDataInfo> stream,
                                             Predicate<Actor> filter,
                                             Comparator<Actor> comparator) {

    return stream.map(annualAwardDataInfo -> annualAwardDataInfo.withYearOfBirth(annualAwardDataInfo.year - annualAwardDataInfo.age))
             .collect(Collectors.groupingBy(annualAwardDataInfo -> new Actor(annualAwardDataInfo.name, annualAwardDataInfo.actorYearOfBirth),
               Collectors.counting()))
             .entrySet().stream()
             .map(entry -> entry.getKey().withNumberOfAwards(entry.getValue()))
             .filter(filter)
             .sorted(comparator);
  }

  public Stream<Actor> aggregateAwardDataFromCSVFiles(
    Predicate<Actor> filter,
    Comparator<Actor> comparator, Path...paths) {

    return aggregateAwardData(Stream.of(paths).flatMap((Path path) -> {
        try {
          return Files.lines(path);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }).filter(l -> !l.startsWith("#")).map(l -> l.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")).map(AnnualAwardDataInfo::fromArray),
      filter, comparator);
  }
}

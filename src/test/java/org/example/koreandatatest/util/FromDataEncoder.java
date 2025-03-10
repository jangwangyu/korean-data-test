package org.example.koreandatatest.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.web.util.UriComponentsBuilder;

@TestComponent
public class FromDataEncoder {

  private final ObjectMapper mapper;

  public FromDataEncoder(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  /**
   *  데이터를 post form data 형식으로 인코딩 한다.
   *  @param obj 인코딩할 데이터
   *  @return from data 형식으로 인코딩된 문자열
   */

  public String encode(Object obj) {
    Map<String, Object> map = mapper.convertValue(obj, new TypeReference<>() {});
    UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

    map.forEach((key, value) -> addToBuilder(builder, key, value));
    return builder.build().encode().getQuery();
  }

  private void addToBuilder(UriComponentsBuilder builder, String key, Object value) {
    switch (value) {
      case Map<?, ?> map -> map.forEach((subKey, subValue) -> addToBuilder(builder, key + "." + subKey, subValue));
      case List<?> list -> IntStream.range(0, list.size()).forEach(i -> addToBuilder(builder, key +"[" + i + "]", list.get(i)));
      case null -> {}
      default -> builder.queryParam(key, value.toString());
    }
  }
}

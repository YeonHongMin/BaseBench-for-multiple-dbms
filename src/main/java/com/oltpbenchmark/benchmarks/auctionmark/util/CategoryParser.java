/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Apache License, Version 2.0 (이하 "라이센스")에 따라 라이센스가 부여됩니다.
 * 이 파일은 라이센스에 따라 사용할 수 있으며, 라이센스에 따라 사용하지 않는 한
 * 사용할 수 없습니다. 라이센스 사본은 다음에서 얻을 수 있습니다.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * 적용 가능한 법률에 의해 요구되거나 서면으로 합의되지 않는 한, 라이센스에 따라
 * 배포되는 소프트웨어는 "있는 그대로" 배포되며, 명시적이거나 묵시적인 어떠한 종류의
 * 보증이나 조건도 없습니다. 라이센스에 따른 권한 및 제한 사항에 대한 자세한 내용은
 * 라이센스를 참조하십시오.
 *
 */

package com.oltpbenchmark.benchmarks.auctionmark.util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CategoryParser {
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(CategoryParser.class);

  Map<String, Category> _categoryMap;
  private int _nextCategoryID;
  String _fileName;

  public CategoryParser() {

    _categoryMap = new TreeMap<>();
    _nextCategoryID = 0;

    final String path = "/benchmarks/auctionmark/table.category";

    try (InputStream resourceAsStream = this.getClass().getResourceAsStream(path)) {

      List<String> lines = IOUtils.readLines(resourceAsStream, Charset.defaultCharset());
      for (String line : lines) {
        extractCategory(line);
      }

    } catch (Exception ex) {
      throw new RuntimeException("Failed to load in category file", ex);
    }
  }

  public void extractCategory(String s) {
    String[] tokens = s.split("\t");
    int itemCount = Integer.parseInt(tokens[5]);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i <= 4; i++) {
      if (!tokens[i].trim().isEmpty()) {
        sb.append(tokens[i].trim()).append("/");
      } else {
        break;
      }
    }
    String categoryName = sb.toString();
    if (categoryName.length() > 0) {
      categoryName = categoryName.substring(0, categoryName.length() - 1);
    }

    addNewCategory(categoryName, itemCount, true);
  }

  public Category addNewCategory(String fullCategoryName, int itemCount, boolean isLeaf) {
    Category category = null;
    Category parentCategory = null;

    String categoryName = fullCategoryName;
    String parentCategoryName = "";
    Integer parentCategoryID = null;

    if (categoryName.indexOf('/') != -1) {
      int separatorIndex = fullCategoryName.lastIndexOf('/');
      parentCategoryName = fullCategoryName.substring(0, separatorIndex);
      categoryName = fullCategoryName.substring(separatorIndex + 1);
    }
    /*
    System.out.println("parentCat name = " + parentCategoryName);
    System.out.println("cat name = " + categoryName);
    */
    if (_categoryMap.containsKey(parentCategoryName)) {
      parentCategory = _categoryMap.get(parentCategoryName);
    } else if (!parentCategoryName.isEmpty()) {
      parentCategory = addNewCategory(parentCategoryName, 0, false);
    }

    if (parentCategory != null) {
      parentCategoryID = parentCategory.getCategoryID();
    }

    category = new Category(_nextCategoryID++, categoryName, parentCategoryID, itemCount, isLeaf);

    _categoryMap.put(fullCategoryName, category);

    return category;
  }

  public Map<String, Category> getCategoryMap() {
    return _categoryMap;
  }
}

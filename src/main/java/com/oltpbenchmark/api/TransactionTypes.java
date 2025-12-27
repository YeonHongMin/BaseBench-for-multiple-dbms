/*
 * Copyright 2020 by OLTPBenchmark Project
 *
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
<<<<<<< HEAD
=======
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
>>>>>>> master
=======
>>>>>>> dbb7887be8f21268712f8dedb24a63633f721d2d
 *
 */

package com.oltpbenchmark.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.map.ListOrderedMap;

public class TransactionTypes implements Collection<TransactionType> {

  private final ListOrderedMap<String, TransactionType> types = new ListOrderedMap<>();

  public TransactionTypes(List<TransactionType> transactiontypes) {
    transactiontypes.sort(TransactionType::compareTo);
    for (TransactionType tt : transactiontypes) {
      String key = tt.getName().toUpperCase();
      this.types.put(key, tt);
    }
  }

  public TransactionType getType(String procName) {
    return (this.types.get(procName.toUpperCase()));
  }

  public TransactionType getType(Class<? extends Procedure> procClass) {
    return (this.getType(procClass.getSimpleName()));
  }

  public TransactionType getType(int id) {
    return (this.types.getValue(id));
  }

  @Override
  public String toString() {
    return this.types.values().toString();
  }

  @Override
  public boolean add(TransactionType tt) {
    String key = tt.getName().toUpperCase();
    this.types.put(key, tt);
    return (true);
  }

  @Override
  public boolean addAll(Collection<? extends TransactionType> c) {
    return false;
  }

  @Override
  public void clear() {
    this.types.clear();
  }

  @Override
  public boolean contains(Object o) {
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return (this.types.values().containsAll(c));
  }

  @Override
  public boolean isEmpty() {
    return (this.types.isEmpty());
  }

  @Override
  public Iterator<TransactionType> iterator() {
    return (this.types.values().iterator());
  }

  @Override
  public boolean remove(Object o) {
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return false;
  }

  @Override
  public int size() {
    return (this.types.size());
  }

  @Override
  public Object[] toArray() {
    return (this.types.values().toArray());
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return (this.types.values().toArray(a));
  }
}

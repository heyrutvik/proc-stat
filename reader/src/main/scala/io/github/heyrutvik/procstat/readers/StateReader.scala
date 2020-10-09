package io.github.heyrutvik.procstat.readers

trait StateReader[T] {
  def read: T
}

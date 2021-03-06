/**
 * Copyright (C) 2015 Baifendian Corporation
 *
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
 */
package cn.com.warlock

import org.apache.spark._

import scala.math.random

object SparkPi {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark Pi")
    val spark = new SparkContext(conf)
    val slices = if (args.length > 0) args(0).toInt else 2

    // slices 对应于 partition 个数，平均每个 partition 有 100000L 个元素
    val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow

    // 这里可以理解为一个循环，每个 partition 上循环次数为 100000L
    val count = spark.parallelize(1 until n, slices).map { i =>
        val x = random * 2 - 1 // random return double value between [0.0, 1.0], so random * 2 - 1 return value between [-1.0, 1.0]
        val y = random * 2 - 1

        if (x * x + y * y < 1) 1 else 0
      }.reduce(_ + _)

    println("Pi is roughly " + 4.0 * count / n)
    spark.stop()
  }
}
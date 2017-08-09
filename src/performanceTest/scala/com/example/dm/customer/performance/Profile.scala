package com.example.dm.customer.performance;

trait TestProfile {

  def restResponseTime():Long
  def restResponseTimeMax(): Int

  def soapResponseTime():Long
  def soapResponseTimeMax(): Int

  def apiResponseTime(): Long
  def apiResponseTimeMax(): Int

  def authToken(): String

}
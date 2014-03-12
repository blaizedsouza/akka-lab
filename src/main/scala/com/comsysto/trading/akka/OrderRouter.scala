package com.comsysto.trading.akka

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import com.comsysto.trading.domain.{Ask, Bid, Security}
import com.comsysto.trading.akka.OrderRouter.{ListSecuritiesResponse, ListSecurities}
import com.comsysto.trading.algorithm.{AverageMarketPriceCalculator, SimpleTradeMatcher}

object OrderRouter {
  case object ListSecurities
  case class ListSecuritiesResponse(securities : List[Security])
}

/**
 * Created by sturmm on 11.03.14.
 */
//TODO: Create a real router (see page 283)
class OrderRouter extends Actor with ActorLogging {
  //Poor mans SecuritiesRepository
  private val securities = List(Security("DE000BAY0017"))

  private val orderBooks = scala.collection.mutable.Map.empty[Security, ActorRef]

  override def preStart() = {
    for (security <- securities) {
      log.debug("Creating order book for " + security)
      orderBooks(security) = context.actorOf(Props[OrderBook](new OrderBook(security) with SimpleTradeMatcher with AverageMarketPriceCalculator))
    }
  }

  def receive = {
    case ListSecurities => sender ! ListSecuritiesResponse(securities)
    //Just forward to the respective order book
    case bid@Bid(sec, _, _) => {
      log.info(s"Received bid for $sec")
      orderBooks(sec) ! bid
    }
    case ask@Ask(sec, _, _) => {
      log.info(s"Received ask for $sec")
      orderBooks(sec) ! ask
    }

    //case m@_ => log.warning("Unexpected message " + m)
  }
}

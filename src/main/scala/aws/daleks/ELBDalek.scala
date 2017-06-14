package aws.daleks

import com.amazonaws.regions.Region
import scala.collection.JavaConverters._
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription
import com.amazonaws.services.elasticloadbalancing.model.DeleteLoadBalancerRequest
import rx.lang.scala._

case class ELBDalek(implicit region: Region) extends RxDalek[LoadBalancerDescription] {
  val elb = withRegion(new AmazonElasticLoadBalancingClient)
  
  override def observe:Observable[LoadBalancerDescription] = elb.describeLoadBalancers
    .getLoadBalancerDescriptions
    .asScala
    .toObservable
    
  override def exterminate(t:LoadBalancerDescription):Unit =
    elb.deleteLoadBalancer(new DeleteLoadBalancerRequest()
        .withLoadBalancerName(t.getLoadBalancerName))
  override def describe(t:LoadBalancerDescription):Map[String,String] = Map("lbName"->t.getLoadBalancerName)


}
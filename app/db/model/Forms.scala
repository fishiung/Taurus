package db.model

import play.api.data.Forms._
import play.api.data.Form

object Forms {

  val targetAttrForm = Form {
    mapping(
      "cnName" -> text,
      "enName" -> text,
      "orId" -> number,
      "otId" -> number,
      "ttId" -> number,
      "atype" -> text,
      "isPK" -> optional(number),
      "isMP" -> optional(number),
      "isMultValue" -> optional(number),
      "isLongValue" -> optional(number),
      "rulePipeline" -> optional(text)
    )(TargetAttrForm.apply)(TargetAttrForm.unapply)
  }


}

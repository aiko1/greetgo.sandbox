import { Pipe, PipeTransform} from '@angular/core';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'dateFormat'
})
export class DateForUI extends DatePipe implements PipeTransform {
  transform(value: any, args?: any): any {
    return super.transform(value, "dd.MM.yyyy");
  }
}
//
// export class DateForDB extends DatePipe implements PipeTransform {
//   transform(value: any, args?: any): any {
//     return super.transform(value, "yyyy-MM-dd");
//   }
// }

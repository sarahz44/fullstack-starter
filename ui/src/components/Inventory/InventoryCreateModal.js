import * as Yup from 'yup'
import Button from '@material-ui/core/Button'
import Checkbox from '@material-ui/core/Checkbox'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import Grid from '@material-ui/core/Grid'
import { MeasurementUnits } from '../../constants/units'
import { MenuItem } from '@material-ui/core'
import React from 'react'
import TextField from '../Form/TextField'
import { Field, Form, Formik } from 'formik'


class InventoryCreateModal extends React.Component {
  render() {
    const {
      formName,
      handleDialog,
      handleInventory,
      title,
      initialValues,
      listProd
    } = this.props

    const validationSchema = Yup.object().shape({
      name: Yup.string().required(),
      productType: Yup.string().required(),
      unitOfMeasurement: Yup.string().required(),
    })
    const label = { inputProps: { 'aria-label': 'Checkbox demo' } }


    return (
      <Dialog
        open={this.props.isDialogOpen}
        maxWidth='sm'
        fullWidth={true}
        onClose={ () => { handleDialog(false) }}
      >
        <Formik
          initialValues={initialValues}
          validationSchema={validationSchema}
          onSubmit={values => {
            const temp = values.bestBeforeDate
            const inputDate = new Date(temp)
            const temp2 = inputDate.toISOString()
            values.bestBeforeDate = temp2
            handleInventory(values)
            handleDialog(true)
          }} >
          {helpers =>
            <Form
              autoComplete='off'
              id={formName}
            >
              <DialogTitle id='alert-dialog-title'>
                {`${title} Inventory`}
              </DialogTitle>
              <DialogContent>
                <Grid container spacing={5}>
                  <Grid item xs ={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='name'
                      label='Name'
                      component={TextField}
                    />
                  </Grid>
                  <Grid item xs ={12} sm={12}>
                    <Field
                      name='productType'
                      select
                      label='Product Type'
                      helperText='Please Select Product Type'
                      component={TextField}
                    >
                      {listProd.map((item) =>
                        <MenuItem key={item.id} value={item.name} >
                          {item.name}
                        </MenuItem>
                      )}
                    </Field>
                  </Grid>
                  <Grid item xs ={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='description'
                      label='Description'
                      component={TextField}
                    />
                  </Grid>
                  <Grid item xs ={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='averagePrice'
                      label='Average Price '
                      component={TextField}
                      type='number'
                    />
                  </Grid>
                  <Grid item xs ={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='amount'
                      label='Amount'
                      component={TextField}
                      type='number'
                    />
                  </Grid>
                  <Grid item xs ={12} sm={12}>
                    <Field
                      name='unitOfMeasurement'
                      select
                      label='Unit of Measure'
                      helperText='Please Select Unit of Measure'
                      component={TextField}
                    >
                      {Object.keys(MeasurementUnits).map((units) =>
                        <MenuItem key={units.id} value={units}>
                          {MeasurementUnits[units].name}
                        </MenuItem>
                      )}
                    </Field>
                  </Grid>
                  <Grid item xs ={12}>
                    <Field
                      name='bestBeforeDate'
                      component={TextField}
                      type='date'
                    />
                  </Grid>
                  <Grid item xs ={12} sm={12}>
                    <header>Never Expires:</header>
                    <Field
                      name='neverExpires'
                      component={TextField}
                      type='checkbox'
                    />
                  </Grid>

                </Grid>
              </DialogContent>
              <DialogActions>
                <Button onClick={() => handleDialog(false) } color='secondary'>Cancel</Button>
                <Button
                  disableElevation
                  variant='contained'
                  type='submit'
                  form={formName}
                  color='secondary'
                  disabled={!helpers.dirty}>
                 Save
                </Button>
              </DialogActions>
            </Form>
          }
        </Formik>
      </Dialog>
    )
  }
}

export default InventoryCreateModal
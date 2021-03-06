package link.pple.assets.domain.donation.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import link.pple.assets.domain.Blood
import link.pple.assets.domain.Blood.Product.*
import link.pple.assets.domain.donation.entity.Donation
import link.pple.assets.domain.donation.repository.DonationRepository
import link.pple.assets.domain.patient.entity.Patient
import link.pple.assets.domain.patient.service.PatientCommand
import link.pple.assets.domain.patient.service.PatientDefinition
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo

/**
 * @Author Heli
 */
@ExtendWith(MockKExtension::class)
internal class DonationCommandTest {

    @MockK
    private lateinit var donationRepository: DonationRepository

    @MockK
    private lateinit var donationQuery: DonationQuery

    @MockK
    private lateinit var patientCommand: PatientCommand

    @InjectMockKs
    private lateinit var sut: DonationCommand

    @Test
    fun `Donation 을 생성할 수 있다`() {
        // given
        val definition = DonationDefinition(
            title = "title",
            content = "content",
            bloodProduct = listOf(
                WHOLE,
                PLATELET,
                LEUKOCYTE,
                PACKED_RED_BLOOD_CELL,
                LEUKOCYTE_REDUCED_RED_BLOOD_CELL
            ),
            patient = PatientDefinition(
                blood = Blood(abo = Blood.ABO.B, rh = Blood.RH.POSITIVE),
            ),
            phoneNumber = "01096081327"
        )
        val patient = Patient.create(
            blood = definition.patient.blood,
        )
        val donation = Donation.create(
            title = definition.title,
            content = definition.content,
            bloodProduct = definition.bloodProduct,
            patient = patient,
            phoneNumber = definition.phoneNumber
        )
        every { patientCommand.create(definition.patient) } returns patient
        every { donationRepository.save(any()) } returns donation

        // when
        val actual = sut.create(definition = definition)

        // then
        expectThat(actual) {
            get { title } isEqualTo "title"
            get { content } isEqualTo "content"
            get { bloodProduct } isEqualTo listOf(
                WHOLE,
                PLATELET,
                LEUKOCYTE,
                PACKED_RED_BLOOD_CELL,
                LEUKOCYTE_REDUCED_RED_BLOOD_CELL
            )
            get { patient }.isNotEqualTo(null).and {
                get { blood } isEqualTo Blood(abo = Blood.ABO.B, rh = Blood.RH.POSITIVE)
                get { status } isEqualTo Patient.Status.ACTIVE
            }
            get { phoneNumber } isEqualTo "01096081327"

            println("WOW!")
        }

    }

    /**
     * @author Ko
     */
    @Test
    fun `Donation 을 수정할 수 있다`() {
        // given
        val definition = DonationDefinition(
            title = "title",
            content = "content",
            bloodProduct = listOf(
                WHOLE,
                PLATELET,
                LEUKOCYTE,
                PACKED_RED_BLOOD_CELL,
                LEUKOCYTE_REDUCED_RED_BLOOD_CELL
            ),
            patient = PatientDefinition(
                blood = Blood(abo = Blood.ABO.B, rh = Blood.RH.POSITIVE),
            ),
            phoneNumber = "01096081327"
        )
        val patient = Patient.create(
            blood = definition.patient.blood,
        )
        val donation = Donation.create(
            title = definition.title,
            content = definition.content,
            bloodProduct = definition.bloodProduct,
            patient = patient,
            phoneNumber = definition.phoneNumber
        )
        every { patientCommand.create(definition.patient) } returns patient
        every { donationRepository.save(any()) } returns donation

        // when
        val actual = sut.create(definition = definition)
        val listOf = listOf(
            WHOLE,
            PLATELET,
            LEUKOCYTE,
            PACKED_RED_BLOOD_CELL,
            LEUKOCYTE_REDUCED_RED_BLOOD_CELL
        )

        println("actual ${listOf.equals(actual.bloodProduct) }")

        // then
        expectThat(actual) {
            get { title } isEqualTo "title"
            get { content } isEqualTo "content"
            get { bloodProduct } isEqualTo listOf(
                WHOLE,
                PLATELET,
                LEUKOCYTE,
                PACKED_RED_BLOOD_CELL,
                LEUKOCYTE_REDUCED_RED_BLOOD_CELL
            )
            get { patient }.isNotEqualTo(null).and {
                get { blood } isEqualTo Blood(abo = Blood.ABO.B, rh = Blood.RH.POSITIVE)
                get { status } isEqualTo Patient.Status.ACTIVE
            }
            get { phoneNumber } isEqualTo "01096081327"
        }

        println("================================")
        val updatedDefinition = DonationDefinition(
            title = "title2",
            content = "content2",
            bloodProduct = listOf(
                WHOLE,
                PLATELET,
                LEUKOCYTE,
                PACKED_RED_BLOOD_CELL,
                LEUKOCYTE_REDUCED_RED_BLOOD_CELL
            ),
            patient = PatientDefinition(
                blood = Blood(abo = Blood.ABO.B, rh = Blood.RH.POSITIVE),
            ),
            phoneNumber = "01039118369"
        )

        val updatedDonation = Donation.create(
            title = updatedDefinition.title,
            content = updatedDefinition.content,
            bloodProduct = updatedDefinition.bloodProduct,
            patient = patient,
            phoneNumber = updatedDefinition.phoneNumber
        )

        every { donationRepository.save(any()) } returns updatedDonation

        val updateActual = sut.update(donation = updatedDonation, dto = updatedDefinition)

        println("updateActual ${listOf.equals(updateActual.bloodProduct) }")

        expectThat(updateActual) {
            get { title } isNotEqualTo "title"
            get { content } isNotEqualTo "content"
            get { bloodProduct } isEqualTo listOf(
                WHOLE,
                PLATELET,
                LEUKOCYTE,
                PACKED_RED_BLOOD_CELL,
                LEUKOCYTE_REDUCED_RED_BLOOD_CELL
            )
            get { patient }.isNotEqualTo(null).and {
                get { blood } isEqualTo  Blood(abo = Blood.ABO.B, rh = Blood.RH.POSITIVE)
                get { status } isEqualTo  Patient.Status.ACTIVE
            }
            get { phoneNumber } isNotEqualTo "01096081327"
        }
    }

}
